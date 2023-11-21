import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ConfirmationService } from 'primeng/api';
import { Table } from 'primeng/table';
import { FamilyAndFriendsService } from 'src/app/core/service/customer-care/family-and-friends.service';
import { FootPrintService } from 'src/app/core/service/foot-print.service';
import { SubscriberService } from 'src/app/core/service/subscriber.service';
import { FootPrint } from 'src/app/shared/models/foot-print.interface';
import { FeaturesService } from 'src/app/shared/services/features.service';
import { MessageService } from 'src/app/shared/services/message.service';
import { ToastService } from 'src/app/shared/services/toast.service';
import { ValidationService } from 'src/app/shared/services/validation.service';

@Component({
    selector: 'app-family-and-friends',
    templateUrl: './family-and-friends.component.html',
    styleUrls: ['./family-and-friends.component.scss'],
    providers: [ConfirmationService],
})
export class FamilyAndFriendsComponent implements OnInit {
    planID: any;

    constructor(
        private fb: FormBuilder,
        private fAFService: FamilyAndFriendsService,
        private validation: ValidationService,
        private toastService: ToastService,
        private messageService: MessageService,
        private footPrintService: FootPrintService,
        private featuresService: FeaturesService,
        private confirmationService: ConfirmationService
    ) { }
    types = [];
    isDisabled = false;
    fAFPlans$ = this.fAFService.allFAFPlansSubject$;
    rowSelected = null;
    loading$ = this.fAFService.loading$;
    fAFPlansLookup$ = this.fAFService.FAFPlansLookupSubject$;
    addPlanDialog: boolean;
    updatePlanDialog: boolean;
    fAFForm: FormGroup;
    updatefAFForm: FormGroup;
    oldNumber;
    permissions = {
        deleteFamilyAndFriends: false,
        UpdateFamilyAndFriends: false,
        addFamilyAndFriends: false,
        getFamilyAndFriends: false,
    };
    search = false;
    searchText: string;
    ngOnInit(): void {
        this.setPermissions();
        this.createForm();
        this.updateForm();
        this.fAFService.getFAFPlansLookup();

        // foot print load
        let footprintObj: FootPrint = {
            machineName: +sessionStorage.getItem('machineName') ? sessionStorage.getItem('machineName') : null,
            profileName: JSON.parse(sessionStorage.getItem('session')).userProfile.profileName,
            pageName: 'Family and friends',
            msisdn: JSON.parse(sessionStorage.getItem('msisdn')),
        };
        this.footPrintService.log(footprintObj);
        if (this.permissions.getFamilyAndFriends) {
            this.fAFService.getFAFPlans();
        } else {
            this.toastService.error(this.messageService.getMessage(401).message, 'Error');
        }
    }
    createForm() {
        this.fAFForm = this.fb.group({
            planId: ['', Validators.required],
            number: [
                '',
                [
                    Validators.required,
                    Validators.maxLength(10),
                    Validators.pattern(this.validation.msisdnPattern),
                    Validators.minLength(10),
                ],
            ],
        });
    }
    updateForm() {
        this.updatefAFForm = this.fb.group({
            planId: ['', Validators.required],
            number: [
                '',
                [
                    Validators.required,
                    Validators.maxLength(10),
                    Validators.pattern(this.validation.msisdnPattern),
                    Validators.minLength(10),
                ],
            ],
        });
    }
    onRowSelect(event) {
        this.isDisabled = true;
        this.rowSelected = event.data;
        // let planLookup = this.fAFPlansLookup$.value.filter(element => element.name === this.rowSelected.plan);
        // this.planID = planLookup[0].planId
        this.fAFForm.patchValue({
            planId: 800,
            number: this.rowSelected.number,
        });
    }
    deleteFAF(id, number) {
        ////set planId static until return from list
        let planObj = {
            familyAndFriendsPlanId: id,
            familyAndFriendsNumber: number,
            footprint: {
                machineName: sessionStorage.getItem('machineName') ? sessionStorage.getItem('machineName') : null,
                profileName: JSON.parse(sessionStorage.getItem('session')).userProfile.profileName,
                pageName: 'Family and Friends',
                footPrintDetails: [
                    {
                        paramName: 'familyAndFriendsPlanId',
                        oldValue: '',
                        newValue: id,
                    },
                    {
                        paramName: 'familyAndFriendsNumber',
                        oldValue: '',
                        newValue: number,
                    },
                ],
            },
        };
        this.fAFService.deleteFAFPlan(planObj);
    }

    ShowAddFaf() {
        this.addPlanDialog = true;
    }

    ShowUpdateFaf(plan) {
        this.updatePlanDialog = true;
        this.oldNumber = plan.number;
        this.updatefAFForm.patchValue({
            planId: plan.planId,
            number: plan.number,
        });
    }

    addNewFAF() {
        if (this.checkNumberFound(this.fAFForm.value.number) === undefined) {
            let planObj = {
                familyAndFriendsPlanId: this.fAFForm.value.planId,
                familyAndFriendsNumber: this.fAFForm.value.number,
                footprint: {
                    machineName: sessionStorage.getItem('machineName') ? sessionStorage.getItem('machineName') : null,
                    profileName: JSON.parse(sessionStorage.getItem('session')).userProfile.profileName,
                    pageName: 'Family and Friends',
                    footPrintDetails: [
                        {
                            paramName: 'familyAndFriendsPlanId',
                            oldValue: '',
                            newValue: this.fAFForm.value.planId,
                        },
                        {
                            paramName: 'familyAndFriendsNumber',
                            oldValue: '',
                            newValue: this.fAFForm.value.number,
                        },
                    ],
                },
            };
            if (this.fAFForm.valid) {
                this.fAFService.addFAFPlan(planObj).subscribe((res) => {
                    if (res.statusCode === 0) {
                        this.fAFService.getFAFPlans();
                        this.addPlanDialog = false;
                        this.toastService.success('Faf Plan Added Successfully');
                        this.createForm()
                    }
                });
            }
        } else {
            this.toastService.error(this.messageService.getMessage(90).message);
        }
    }
    updateFAFPlan() {
        let planObj = {
            familyAndFriendsPlanId: this.updatefAFForm.value.planId,
            familyAndFriendsNumber: this.updatefAFForm.value.number,
            footprint: {
                machineName: sessionStorage.getItem('machineName') ? sessionStorage.getItem('machineName') : null,
                profileName: JSON.parse(sessionStorage.getItem('session')).userProfile.profileName,
                pageName: 'Family and Friends',
                footPrintDetails: [
                    {
                        paramName: 'familyAndFriendsPlanId',
                        oldValue: '',
                        newValue: this.updatefAFForm.value.planId,
                    },
                    {
                        paramName: 'familyAndFriendsNumber',
                        oldValue: this.oldNumber,
                        newValue: this.updatefAFForm.value.number,
                    },
                ],
            },
        };
        if (this.updatefAFForm.valid) {
            this.fAFService.updateFAFPlan(planObj);
            this.createForm()
            this.updatePlanDialog = false;
            this.fAFService.getFAFPlans();
        }
    }

    checkNumberFound(number) {
        if (this.fAFPlans$.value) {
            return this.fAFPlans$.value.find((el) => el.number === number);
        }

    }
    confirm(id, number) {
        console.log(id, number);

        this.confirmationService.confirm({
            message: this.messageService.getMessage(37).message,
            accept: () => {
                this.deleteFAF(id, number);
            },
        });
    }

    setPermissions() {
        let findSubscriberPermissions: Map<number, string> = new Map()
            .set(222, 'UpdateFamilyAndFriends')
            .set(221, 'addFamilyAndFriends')
            .set(220, 'deleteFamilyAndFriends')
            .set(219, 'getFamilyAndFriends');
        this.featuresService.checkUserPermissions(findSubscriberPermissions);
        this.permissions.UpdateFamilyAndFriends = this.featuresService.getPermissionValue(222);
        this.permissions.addFamilyAndFriends = this.featuresService.getPermissionValue(221);
        this.permissions.deleteFamilyAndFriends = this.featuresService.getPermissionValue(220);
        this.permissions.getFamilyAndFriends = this.featuresService.getPermissionValue(219);
    }
    clear(table: Table) {
        this.searchText = ''
        table.clear();
    }
}
