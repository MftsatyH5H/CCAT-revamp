import {Component, OnDestroy, OnInit} from '@angular/core';
import {Table} from 'primeng/table';
import {BehaviorSubject, Subscription} from 'rxjs';
import {map, tap} from 'rxjs/operators';
import {ProductsViewService} from 'src/app/core/service/customer-care/products-view.service';
import {FootPrintService} from 'src/app/core/service/foot-print.service';
import {FootPrint} from 'src/app/shared/models/foot-print.interface';
import {Calendar} from 'primeng/calendar';
import {SubscriberService} from 'src/app/core/service/subscriber.service';
Calendar.prototype.getDateFormat = () => 'dd/mm/yy';
@Component({
    selector: 'app-products-view',
    templateUrl: './products-view.component.html',
    styleUrls: ['./products-view.component.scss'],
})
export class ProductsViewComponent implements OnInit ,OnDestroy {
    constructor(
        private productViewService: ProductsViewService,
        private footPrintService: FootPrintService,
        private subscriberService: SubscriberService
    ) {}
    ngOnDestroy(): void {
        this.isOpenedSubscriber.unsubscribe()
        this.isOpenedNavSubscriber.unsubscribe()
    }
    loading = false;
    products$;
    showQuotas = false;
    productQuotas = [];
    isopened : boolean
    isopenedNav :boolean
    isOpenedSubscriber : Subscription
    isOpenedNavSubscriber :Subscription;
    search=false;
    searchText:string;
    ngOnInit(): void {
        this.loading = true;
        this.isOpenedSubscriber =this.subscriberService.giftOpened.subscribe(isopened=>{
            this.isopened = isopened
        })
        this.isOpenedNavSubscriber =this.subscriberService.sidebarOpened.subscribe(isopened=>{
            this.isopenedNav = isopened
        })
        this.products$ = this.productViewService.getAllProducts$.pipe(
            tap(
                (res) => {
                    this.loading = false;
                    // footprint
                    let footprintObj: FootPrint = {
                        machineName: +sessionStorage.getItem('machineName')
                            ? sessionStorage.getItem('machineName')
                            : null,
                        profileName: JSON.parse(sessionStorage.getItem('session')).userProfile.profileName,
                        pageName: 'Products View',
                        msisdn: JSON.parse(sessionStorage.getItem('msisdn')),
                        footPrintDetails: [
                            {
                                parameterName: 'msisdn',
                                oldValue: null,
                                newValue: JSON.parse(sessionStorage.getItem('msisdn')),
                            },
                        ],
                    };
                    this.footPrintService.log(footprintObj);
                },
                (err) => {
                    this.loading = false;
                }
            ),
            map((res) =>
                res.payload?.products?.product.map((product) => {
                    return {
                        ...product,
                        productStartDate: product.productStartDate ? new Date(product.productStartDate) : null,
                        productExpiryDate: product.productExpiryDate ? new Date(product.productExpiryDate) : null,
                        productRenewalDate: product.productRenewalDate ? new Date(product.productRenewalDate) : null,
                    };
                })
            )
        );

        // footprint
        let footprintObj: FootPrint = {
            machineName: +sessionStorage.getItem('machineName') ? sessionStorage.getItem('machineName') : null,
            profileName: JSON.parse(sessionStorage.getItem('session')).userProfile.profileName,
            pageName: 'Products View',
            msisdn: JSON.parse(sessionStorage.getItem('msisdn')),
        };
        this.footPrintService.log(footprintObj);
    }
    onRowSelect(event) {
        this.showQuotas = true;
        this.productQuotas = event.data.quotas;
        console.log(this.productQuotas);
    }
    clear(table: Table) {
        table.clear();
    }

    get dashDate() {
        return new Date(253370757600000);
    }
}
