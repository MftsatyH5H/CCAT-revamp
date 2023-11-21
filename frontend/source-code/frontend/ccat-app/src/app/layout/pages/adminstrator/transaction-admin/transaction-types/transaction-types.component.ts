import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { ConfirmationService } from 'primeng/api';
import { Table } from 'primeng/table';
import { TransactionAdminService } from 'src/app/core/service/administrator/transaction-admin.service';
import { TransactionType } from 'src/app/shared/models/transaction-admin.model';
import { MessageService } from 'src/app/shared/services/message.service';
import { ToastService } from 'src/app/shared/services/toast.service';
import { ValidationService } from 'src/app/shared/services/validation.service';

@Component({
    selector: 'app-transaction-types',
    templateUrl: './transaction-types.component.html',
    styleUrls: ['./transaction-types.component.scss'],
    providers: [ConfirmationService],
})
export class TransactionTypesComponent implements OnInit {
    searchText: any;
    constructor(
        private transactionTypesServices: TransactionAdminService,
        private toastrService: ToastService,
        private messageService: MessageService,
        private confirmationService: ConfirmationService,
        private validationService: ValidationService
    ) { }
    @Input() permissions: any;
    @ViewChild('nameInput') nameInput: ElementRef;
    filterSearch;
    typesTable: TransactionType[];
    types: TransactionType[];
    openDialog: boolean;
    loading$ = this.transactionTypesServices.loading$;
    typesForm: FormGroup;
    submittedTransactionType: TransactionType = { name: '', value: '', ccFeatures: [] };
    editMode: boolean = false;
    fixedCCFeatures = {
        customerBalance: 2,
        prepaidVBP: 6,
    };
    // name and value similiraty boolean
    similarityError = false;
    similarityErrorMsg = '';
    ngOnInit(): void {
        this.setForm();
        this.getTransactionsTypes();
    }
    checkAuthorized() {
        if (
            !this.permissions.getAllTypes &&
            !this.permissions.addType &&
            !this.permissions.updateType &&
            !this.permissions.deleteType
        ) {
            return true;
        } else {
            return false;
        }
    }
    getTransactionsTypes() {
        if (this.permissions.getAllTypes) {
            this.transactionTypesServices.transactionsTypes$.subscribe((transactionsTypes) => {
                this.typesTable = transactionsTypes;
                this.types = transactionsTypes;
                console.log(this.types);
            });
        } else {
            this.toastrService.error('Error', this.messageService.getMessage(401).message);
        }
    }

    filterTable() {
        if (this.filterSearch) {
            const filteredTable = this.types.filter((type) =>
                type.name.toLocaleLowerCase().match(this.filterSearch.toLocaleLowerCase())
            );
            this.typesTable = filteredTable;
        } else {
            this.typesTable = this.types;
        }
    }
    confirmDeleteType(id: number) {
        this.confirmationService.confirm({
            message: this.messageService.getMessage(26).message,
            accept: () => {
                this.deleteTransactionType(id);
            },
        });
    }
    deleteTransactionType(id: number) {
        this.transactionTypesServices.deleteTransactionType(id).subscribe((resp) => {
            if (resp.statusCode === 0) {
                this.toastrService.success('Success', this.messageService.getMessage(27).message);
                this.getTransactionsTypes();
            }
        });
    }
    addTransactionType() {
        this.editMode = false;
        this.submittedTransactionType = { name: '', value: '', ccFeatures: [] };
        this.setForm();
        this.openDialog = true;
    }
    updateTransactionType(transactionType: TransactionType) {
        this.editMode = true;
        this.submittedTransactionType = transactionType;
        this.setForm();
        this.openDialog = true;
    }
    unCheckElement(event, element) {
        if (!event.checked) {
            this.submittedTransactionType.ccFeatures = this.submittedTransactionType.ccFeatures.filter(
                (el) => el !== element
            );
        }
    }
    setForm() {
        this.typesForm = new FormGroup({
            typeName: new FormControl(this.submittedTransactionType.name, [
                Validators.required,
                Validators.pattern(this.validationService.whiteSpacesPattern),
            ]),
            typeValue: new FormControl(this.submittedTransactionType.value, [
                Validators.required,
                Validators.pattern(this.validationService.whiteSpacesPattern),
            ]),
            customerBalance: new FormControl(
                this.submittedTransactionType.ccFeatures.includes(this.fixedCCFeatures.customerBalance)
            ),
            prepaidVBP: new FormControl(
                this.submittedTransactionType.ccFeatures.includes(this.fixedCCFeatures.prepaidVBP)
            ),
            ...(this.editMode && { id: new FormControl(this.submittedTransactionType.id) }),
        });
    }
    closeDialog() {
        this.similarityError = false;
        this.similarityErrorMsg = '';
        this.openDialog = false;
    }
    submit() {
        if (
            this.typesForm.value['customerBalance'] &&
            !this.submittedTransactionType.ccFeatures.includes(this.fixedCCFeatures.customerBalance)
        ) {
            this.submittedTransactionType.ccFeatures.push(this.fixedCCFeatures.customerBalance);
        }
        if (
            this.typesForm.value['prepaidVBP'] &&
            !this.submittedTransactionType.ccFeatures.includes(this.fixedCCFeatures.prepaidVBP)
        ) {
            this.submittedTransactionType.ccFeatures.push(this.fixedCCFeatures.prepaidVBP);
        }
        const reqObj: TransactionType = {
            name: this.typesForm.value['typeName'],
            value: this.typesForm.value['typeValue'],
            ...(this.editMode && { id: this.typesForm.value['id'] }),
            ccFeatures: this.submittedTransactionType.ccFeatures,
        };

        if (!this.editMode) {
            // check for value !== type

            if (reqObj.name.toLocaleLowerCase().trim() === reqObj.value.toLocaleLowerCase().trim()) {
                this.openDialog = false;
                this.toastrService.warning('Type and Value names should be different');
                return;
            }
            // check for value or type aren't duplicated
            else if (

                this.types.find(
                    (el) => el.name.toLocaleLowerCase().trim() === reqObj.name.toLocaleLowerCase().trim()
                )
            ) {
                this.openDialog = false;
                this.toastrService.warning('Type Name Is Already Exist');
                return;
            } else if (
                this.types.find((el) => el.value.toLocaleLowerCase().trim() === reqObj.value.toLocaleLowerCase().trim())
            ) {
                this.openDialog = false;
                this.toastrService.warning('Type Value Is Already Exist');
                return;
            } else {
                this.transactionTypesServices.addTransactionType(reqObj).subscribe((resp) => {
                    if (resp.statusCode === 0) {
                        this.toastrService.success(this.messageService.getMessage(30).message);
                        this.getTransactionsTypes();
                    }
                });

            }
        } else {
            if (this.chechSimilrityEditMode()) {
                this.toastrService.warning('Name is duplicated');
            } else {
                this.transactionTypesServices.updateTransactionType(reqObj).subscribe((resp) => {
                    if (resp.statusCode === 0) {
                        this.toastrService.success(this.messageService.getMessage(31).message);
                        this.getTransactionsTypes();
                    }
                });
            }
        }
        this.openDialog = false;
    }
    chechSimilrityEditMode() {
        return this.types.find(
            (el) =>
                el.name.toLocaleLowerCase().trim() === this.typesForm.value.typeName.toLocaleLowerCase().trim() &&
                this.typesForm.value.id !== el.id
        );
    }
    clear(table: Table) {
        if (table.filters.global["value"]) {
            table.filters.global["value"] = ''
        }
        this.searchText = null;
        table.clear()
    }
    focusNameInput() {
        this.nameInput.nativeElement.focus();
    }
}
