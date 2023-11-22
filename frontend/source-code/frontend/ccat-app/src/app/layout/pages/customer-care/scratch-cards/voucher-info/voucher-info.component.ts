import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ScratchCardsService} from 'src/app/core/service/customer-care/scratch-cards.service';
import {FootPrintService} from 'src/app/core/service/foot-print.service';
import {FootPrint} from 'src/app/shared/models/foot-print.interface';
import {FeaturesService} from 'src/app/shared/services/features.service';
import {ToastService} from 'src/app/shared/services/toast.service';
import {isTemplateSpan} from 'typescript';

@Component({
    selector: 'app-voucher-info',
    templateUrl: './voucher-info.component.html',
    styleUrls: ['./voucher-info.component.scss'],
})
export class VoucherInfoComponent implements OnInit {
    tab = 'view-voucher-details';
    permissions = {
        getVoucherDetails: false,
        updateVoucherState: false,
        viewAllVoucherState: false,
    };
    stateTypes = [
        {
            id: 0,
            name: 'available',
        },
        {id: 1, name: 'used'},
        {id: 2, name: 'damaged'},
        {id: 3, name: 'stolen / missing'},
        {id: 4, name: 'pending'},
        {id: 5, name: 'unavailable'},
    ];
    voucherDetailsForm: FormGroup;
    updateVoucherForm: FormGroup;
    billingNumber;
    billMsisdn;
    voucherSerialNumberLength =+JSON.parse(sessionStorage.getItem("voucherSerialNumberLength"))
    voucherNumberLength =+JSON.parse(sessionStorage.getItem("voucherNumberLength"))
    constructor(
        private scratchCardsService: ScratchCardsService,
        private fb: FormBuilder,
        private featuresService: FeaturesService,
        private toasterService: ToastService,
        private footPrintService: FootPrintService
    ) {}

    ngOnInit(): void {
        this.initializeVoucherDetailsForm();
        this.initializeUpdateVoucherForm();
        this.setPermissions();
        if (!this.permissions.viewAllVoucherState) {
            this.stateTypes = this.stateTypes.filter((item) => item.id == 4 || item.id == 1);
        }
    }

    switchTab(tab) {
        this.tab = tab;
        // footprint
        let footprintObj: FootPrint = {
            machineName: +sessionStorage.getItem('machineName') ? sessionStorage.getItem('machineName') : null,
            profileName: JSON.parse(sessionStorage.getItem('session')).userProfile.profileName,
            pageName: 'Scratch cards',
            msisdn: JSON.parse(sessionStorage.getItem('msisdn')),
            tabName: tab,
        };
        this.footPrintService.log(footprintObj);
    }
    initializeVoucherDetailsForm() {
        this.voucherDetailsForm = this.fb.group({
            voucherSerialNumber: ['', Validators.required],
            serverId: [1],
            agent: [''],
            batchId: [''],
            currency: [''],
            expiryDate: [''],
            operatorId: [''],
            rechargeSource: [''],
            responseCode: [''],
            state: [''],
            subscriberId: [''],
            timeStamp: [''],
            value: [''],
            voucherGroup: [''],
        });
    }
    initializeUpdateVoucherForm() {
        this.updateVoucherForm = this.fb.group({
            voucherSerialNumber: [
                '',
                [
                    Validators.required,
                    Validators.pattern('^[0-9]*$'),
                    Validators.minLength(8),
                    Validators.maxLength(this.voucherSerialNumberLength),
                ],
            ],
            serverId: [1],
            currentState: ['', Validators.required],
            newState: ['', Validators.required],
        });
    }
    getVoucherDetails() {
        this.scratchCardsService
            .getVoucherDetails$(
                this.voucherDetailsForm.controls['voucherSerialNumber'].value,
                this.voucherDetailsForm.controls['serverId'].value
            )
            .subscribe((result) => {
                let voucher = result.payload.voucher;
                this.voucherDetailsForm.patchValue({
                    agent: voucher.agent,
                    batchId: voucher.batchId,
                    currency: voucher.currency,
                    expiryDate: voucher.expiryDate,
                    operatorId: voucher.operatorId,
                    rechargeSource: voucher.rechargeSource,
                    responseCode: voucher.responseCode,
                    state: voucher.state,
                    subscriberId: voucher.subscriberId,
                    timeStamp: voucher.timeStamp,
                    value: voucher.value,
                    voucherGroup: voucher.voucherGroup,
                });
            });
    }

    updateVoucher() {
        if (this.updateVoucherForm.get('currentState').value === this.updateVoucherForm.get('newState').value) {
            this.toasterService.error('Current And New State Should Be Different');
        } else {
            let reqObj = {
                ...this.updateVoucherForm.value,
                footPrint: {
                    machineName: sessionStorage.getItem('machineName') ? sessionStorage.getItem('machineName') : null,
                    profileName: JSON.parse(sessionStorage.getItem('session')).userProfile.profileName,
                    pageName: 'Barrings',
                    msisdn: JSON.parse(sessionStorage.getItem('msisdn')),
                    tabName: 'View Voucher Details',
                    footPrintDetails: [
                        {
                            paramName: 'voucherSerialNumber',
                            oldValue: null,
                            newValue: this.updateVoucherForm.value.voucherSerialNumber,
                        },
                        {
                            paramName: 'serverId',
                            oldValue: null,
                            newValue: this.updateVoucherForm.value.voucherSerialNumber,
                        },
                        {
                            paramName: 'voucherState',
                            oldValue: this.updateVoucherForm.value.currentState,
                            newValue: this.updateVoucherForm.value.newState,
                        },
                    ],
                },
            };
            this.scratchCardsService.updateVoucherDetails$(reqObj).subscribe((result) => {
                this.toasterService.success(result.statusMessage);
                this.updateVoucherForm.reset();
                this.updateVoucherForm.patchValue({
                    serverId: [1],
                });
            });
        }
    }
    getbillPaymentInfo() {
        this.scratchCardsService
            .getPaymentGetwayVoucher(this.voucherDetailsForm.controls['voucherSerialNumber'].value)
            .subscribe((res) => {
                (this.billMsisdn = res.payload.msisdn), (this.billingNumber = res.payload.billingNumber);
            });
    }
    get voucherSerialNumber() {
        return this.updateVoucherForm.get('voucherSerialNumber').value;
    }

    setPermissions() {
        let findSubscriberPermissions: Map<number, string> = new Map()
            .set(217, 'getVoucherDetails')
            .set(218, 'updateVoucherState')
            .set(303, 'viewAllVoucherState');
        this.featuresService.checkUserPermissions(findSubscriberPermissions);
        this.permissions.getVoucherDetails = this.featuresService.getPermissionValue(217);
        this.permissions.updateVoucherState = this.featuresService.getPermissionValue(218);
        this.permissions.viewAllVoucherState = this.featuresService.getPermissionValue(303);
    }
}
