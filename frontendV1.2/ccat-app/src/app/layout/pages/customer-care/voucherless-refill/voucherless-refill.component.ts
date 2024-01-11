import { AfterViewInit, Component, OnInit, ViewChild, ViewChildren } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { map, take } from 'rxjs/operators';
import { NotepadService } from 'src/app/core/service/administrator/notepad.service';
import { VoucherService } from 'src/app/core/service/customer-care/voucherless.service';
import { FootPrintService } from 'src/app/core/service/foot-print.service';
import { FootPrint } from 'src/app/shared/models/foot-print.interface';
import { Note } from 'src/app/shared/models/note.interface';
import { ToastService } from 'src/app/shared/services/toast.service';
import { SubscriberService } from './../../../../core/service/subscriber.service';

@Component({
    selector: 'app-voucherless-refill',
    templateUrl: './voucherless-refill.component.html',
    styleUrls: ['./voucherless-refill.component.scss'],
})
export class VoucherlessRefillComponent implements OnInit {
    loading$ = this.voucherService.loading$;
    vouRefill;
    allVouchersSubject$ = this.voucherService.allVouchers$;
    voucherForm: FormGroup;
    ReasonDialog: boolean;
    reason;
    notes: Note[] = [];
    subscriberNumber;
    @ViewChild('input') myInput;


    constructor(
        private fb: FormBuilder,
        private voucherService: VoucherService,
        private SubscriberService: SubscriberService,
        private notepadService: NotepadService,
        private toasterService: ToastService,
        private footPrintService: FootPrintService
    ) { }

    setFocus() {
        this.myInput.first.nativeElement.focus();
    }
    onAfterShow(event) {
        this.myInput.focusInput();
    }
    ngOnInit(): void {
        this.createForm();
        this.voucherService.getAllVouchers();
        this.SubscriberService.subscriber$
            .pipe(
                map((subscriber) => subscriber?.subscriberNumber),
                take(1)
            )
            .subscribe((res) => (this.subscriberNumber = res));

        // foot print load
        let footprintObj: FootPrint = {
            machineName: +sessionStorage.getItem('machineName') ? sessionStorage.getItem('machineName') : null,
            profileName: JSON.parse(sessionStorage.getItem('session')).userProfile.profileName,
            pageName: 'Voucher-less Refill',
            msisdn: JSON.parse(sessionStorage.getItem('msisdn'))
        }
        this.footPrintService.log(footprintObj);
    }

    createForm() {
        this.voucherForm = this.fb.group({
            amount: [null, [Validators.required, Validators.min(0)]],
            payment: ['', Validators.required],
        });
    }

    onSubmit() {
        this.ReasonDialog = true;
    }
    submitReason() {
        let noteObj = {
            entry: this.reason,
            footPrint: {
                machineName: sessionStorage.getItem('machineName') ? sessionStorage.getItem('machineName') : null,
                profileName: JSON.parse(sessionStorage.getItem('session')).userProfile.profileName,
                pageName: 'Voucherless Refill',
                footPrintDetails: [{
                    paramName: 'entry',
                    oldValue: '',
                    newValue: this.reason
                }]
            }
        }
        this.ReasonDialog = false;
        this.notepadService.addNote(noteObj, this.subscriberNumber).subscribe((success) => {
            const operator = JSON.parse(sessionStorage.getItem('session')).user;
            this.notes.unshift({
                note: this.reason,
                date: new Date().getTime(),
                operator: operator.ntAccount,
            });
            // this.toasterService.success('Success', success.statusMessage);
        });
        this.voucherService.updateVoucher(this.voucherForm.value);

        // foot print update
        let footprintObj: FootPrint = {
            machineName: +sessionStorage.getItem('machineName') ? sessionStorage.getItem('machineName') : null,
            profileName: JSON.parse(sessionStorage.getItem('session')).userProfile.profileName,
            pageName: 'Voucher-less Refill',
            msisdn: JSON.parse(sessionStorage.getItem('msisdn')),
            footPrintDetails: [
                {
                    "paramName": "Payment Profile",
                    "oldValue": null,
                    "newValue": this.voucherForm.value['payment'].profileName
                },
                {
                    "paramName": "Voucherless Refill Amount",
                    "oldValue": null,
                    "newValue": this.voucherForm.value['amount']
                }
            ]
        }
        this.footPrintService.log(footprintObj)
    }
}
