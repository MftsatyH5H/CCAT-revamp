import {TrafficBehaviorService} from './../../../../core/service/customer-care/traffic-behavior.service';
import {AfterViewChecked, AfterViewInit, ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import {map, take} from 'rxjs/operators';
import {ToastService} from 'src/app/shared/services/toast.service';
import {FeaturesService} from 'src/app/shared/services/features.service';
import {indicate} from 'src/app/shared/rxjs/indicate';
import {BehaviorSubject, Subscription} from 'rxjs';
import { SubscriberService } from 'src/app/core/service/subscriber.service';

@Component({
    selector: 'app-traffic-behaviour',
    templateUrl: './traffic-behaviour.component.html',
    styleUrls: ['./traffic-behaviour.component.scss'],
})
export class TrafficBehaviourComponent implements OnInit, AfterViewChecked, OnDestroy {
    constructor(
        private trafficBehaviorService: TrafficBehaviorService,
        private toasterService: ToastService,
        private featuresService: FeaturesService,
        private cdr: ChangeDetectorRef,
        private subscriberService:SubscriberService
    ) {}
    headers;
    reports;
    fromDate;
    toDate;
    permissions = {
        GET_TRAFFIC_BEHAVIOR_REPORT: false,
    };
    dateTime = new Date();
    loading$ = new BehaviorSubject(false);
    classNameCon = '';
    isopened: boolean;
    isopenedNav: boolean;
    isOpenedSubscriber: Subscription;
    isOpenedNavSubscriber: Subscription;
    ngOnInit(): void {
        this.setPermissions();
        this.handelMenusOpen();
    }
    ngOnDestroy(): void {
        this.isOpenedSubscriber.unsubscribe();
        this.isOpenedNavSubscriber.unsubscribe();
    }
    onDateSelectFromDate(event: any) {
        const selectedDate = event;
        const correctedDate = new Date(
            Date.UTC(selectedDate.getFullYear(), selectedDate.getMonth(), selectedDate.getDate())
        );
        this.fromDate = correctedDate;
    }
    onDateSelectToDate(event: any) {
        const selectedDate = event;
        const correctedDate = new Date(
            Date.UTC(selectedDate.getFullYear(), selectedDate.getMonth(), selectedDate.getDate())
        );
        this.toDate = correctedDate;
    }
    ngAfterViewChecked(): void {
        this.cdr.detectChanges();
    }
    getTraffic() {
        const trafficFromtDate = new Date(this.fromDate).getTime();
        const trafficToDate = new Date(this.toDate).getTime();
        this.trafficBehaviorService
            .getTrafficBehavior$(trafficFromtDate, trafficToDate)
            .pipe(
                take(1),
                indicate(this.loading$)
            )
            .subscribe({
                next: (res) => {
                    if(res?.statusCode===0){
                        this.headers = res?.payload.headers;
                        this.reports = res?.payload.data;
                    }else{
                        this.reports=[];
                    }
                    
                },
                error: (err) => {
                    this.reports=[];
                    this.toasterService.error('Error', err);
                },
            });
    }

    setPermissions() {
        let findSubscriberPermissions: Map<number, string> = new Map().set(154, 'GET_TRAFFIC_BEHAVIOR_REPORT');
        this.featuresService.checkUserPermissions(findSubscriberPermissions);
        this.permissions.GET_TRAFFIC_BEHAVIOR_REPORT = this.featuresService.getPermissionValue(154);
    }

    selectStartDate() {
        console.log('start date selected ');
        if (this.fromDate < this.toDate) {
        }
    }
    handelMenusOpen() {
        this.isOpenedSubscriber = this.subscriberService.giftOpened.subscribe((isopened) => {
            this.isopened = isopened;
            this.setResponsiveTableWidth();
        });
        this.isOpenedNavSubscriber = this.subscriberService.sidebarOpened.subscribe((isopened) => {
            this.isopenedNav = isopened;
            this.setResponsiveTableWidth();
        });
    }
    setResponsiveTableWidth() {
        if (this.isopened && this.isopenedNav) {
            this.classNameCon = 'table-width';
        } else if (this.isopened && !this.isopenedNav) {
            this.classNameCon = 'table-width-1';
        } else if (!this.isopened && this.isopenedNav) {
            this.classNameCon = 'table-width-3';
        } else {
            this.classNameCon = 'table-width-2';
        }
    }
}
