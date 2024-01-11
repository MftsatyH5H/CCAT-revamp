export interface subscriberAccount {
    subscriberNumber: string,
    currentPlan: number,
    balance: number,
    currency: string,
    serviceClass: object,
    status: string,
    barringStatus: string,
    language: string,
    activationDate: number,
    supervisionFeePeriodExpiryDate: number,
    supervisionFeePeriod: string,
    serviceFeePeriodExpiryDate: number,
    serviceFeePeriod: string,
    creditClearance: string,
    serviceRemoval: string,
    refillBarredUntil: number,
    negativeBalanceBarDate: number,
    maxServicePeriodExpiry: string,
    maxSupervesionPeriodExpiry: string,
    originalServiceClass: string,
    promotionPlan: string,
    isNegativeBalBarring: boolean,
    id: string,


}