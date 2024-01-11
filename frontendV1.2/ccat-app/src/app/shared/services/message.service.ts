import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root',
})
export class MessageService {
    messages = [
        { id: 1, message: 'Profile Deleted' },
        { id: 2, message: 'Are you sure that you want to delete this profile?' },
        { id: 3, message: 'User profile added' },
        { id: 4, message: 'User profile updated' },
        { id: 5, message: 'Business Plan Deleted' },
        { id: 6, message: 'Are you sure that you want to delete this business plan?' },
        { id: 7, message: 'Business plan added' },
        { id: 8, message: 'Business plan updated' },
        { id: 9, message: 'Marquee updated' },
        { id: 10, message: 'Are you sure that you want to delete this marquee?' },
        { id: 11, message: 'Are you sure that you want to delete all marquees?' },
        { id: 12, message: 'Marquee deleted' },
        { id: 13, message: 'All marquees deleted' },
        { id: 14, message: 'Marquee added' },
        { id: 15, message: 'Note Added' },
        { id: 16, message: 'Notepad Deleted' },
        { id: 17, message: 'Are you sure that you want to delete all notes?' },
        { id: 18, message: 'Usage counter added' },
        { id: 19, message: 'Usage counter updated' },
        { id: 20, message: 'Are you sure that you want to delete this disconnection code ?' },
        { id: 21, message: 'Disconnection code added' },
        { id: 22, message: 'Disconnection code updated' },
        { id: 23, message: 'Disconnection code deleted' },
        { id: 24, message: 'Transaction code deleted' },
        { id: 25, message: 'Are you sure that you want to delete this transaction code?' },
        { id: 26, message: 'Are you sure that you want to delete this transaction type?' },
        { id: 27, message: 'Transaction type deleted' },
        { id: 28, message: 'Transaction code added' },
        { id: 29, message: 'Transaction code updated' },
        { id: 30, message: 'Transaction type added' },
        { id: 31, message: 'Transaction type updated' },
        { id: 32, message: 'Transaction Code was successfully unlinked from selected type' },
        { id: 33, message: 'Are you sure that you want to delete this offer?' },
        { id: 34, message: 'User updated' },
        { id: 35, message: 'User added' },
        { id: 36, message: 'User deleted' },
        { id: 37, message: 'Are you sure that you want to delete this user?' },
        { id: 38, message: 'Service Class added' },
        { id: 39, message: 'Service Class updated' },
        { id: 40, message: 'Are you sure that you want to delete this service class?' },
        { id: 41, message: 'Please Select Row' },
        { id: 42, message: 'Language Updated Successfully' },
        { id: 43, message: 'Service Class Updated Successfully' },
        { id: 44, message: 'Offer Added Successfully' },
        { id: 45, message: 'Offer Deleted Successfully' },
        { id: 46, message: 'Offer Updated Successfully' },
        { id: 48, message: 'Subscriber added successfully' },
        { id: 49, message: 'Subscriber disconnected' },
        { id: 50, message: 'Voucherless Updated Successfully' },
        { id: 51, message: 'Service Class deleted' },
        { id: 52, message: 'Pam added' },
        { id: 53, message: 'Pam updated' },
        { id: 54, message: 'Pam deleted' },
        { id: 55, message: 'Are you sure that you want to delete this PAM?' },
        { id: 56, message: 'Are you sure that you want to unlock?' },
        { id: 57, message: 'Unlocking success' },
        { id: 58, message: 'Transaction Code was successfully linked to selected type' },
        { id: 59, message: 'Title is duplicated' },
        { id: 60, message: 'Threshold added successfully' },
        { id: 61, message: 'Threshold deleted successfully' },
        { id: 62, message: 'Threshold updated successfully' },
        { id: 63, message: 'Are you sure that you want to delete this threshold?' },
        { id: 64, message: 'Customer Balance updated' },
        { id: 65, message: 'System Settings updated succefully' },
        { id: 66, message: 'Are you sure that you want to delete this dynamic page' },
        { id: 67, message: 'Page deleted successfully' },
        { id: 68, message: 'Step Added successfully' },
        { id: 69, message: 'Step Edited Successfully' },
        { id: 70, message: 'Step Deleted Successfully' },
        { id: 71, message: 'Are you sure that you want to delete this step' },
        { id: 72, message: 'Unbar successfully' },
        { id: 73, message: 'Barring successfully' },
        { id: 74, message: 'Unbar Refill successfully' },
        { id: 75, message: 'Community Added Successfully' },
        { id: 76, message: 'Community Edited Successfully' },
        { id: 77, message: 'Community Deleted Successfully' },
        { id: 78, message: 'Are you sure that you want to delete this community admin' },
        { id: 79, message: 'Account Added Successfully' },
        { id: 80, message: 'Account Updated Successfully' },
        { id: 81, message: 'Account Deleted Successfully' },
        { id: 82, message: 'Are you sure that you want to delete this account group' },
        { id: 83, message: 'Are you sure that you want to delete this FAF Plan admin' },
        { id: 84, message: 'FAF Plan Added Successfully' },
        { id: 85, message: 'FAF Plan Updated Successfully' },
        { id: 86, message: 'FAF Plan Deleted Successfully' },
        { id: 87, message: 'Community Updated Successfully' },
        { id: 88, message: 'Subcription Successfully' },
        { id: 89, message: 'UnSubcription Successfully' },
        { id: 90, message: 'Number already exist' },
        { id: 91, message: 'Enter empty field' },
        { id: 92, message: 'Are you sure that you want to delete this direction' },
        { id: 93, message: 'Are you sure that you want to delete this family' },
        { id: 94, message: 'Are you sure that you want to delete this reason type' },
        { id: 95, message: 'Are you sure that you want to delete this reason' },
        { id: 96, message: 'Direction Added Successfully' },
        { id: 97, message: 'Direction Updated Successfully' },
        { id: 98, message: 'Direction Deleted Successfully' },
        { id: 99, message: 'Family Added Successfully' },
        { id: 100, message: 'Family Updated Successfully' },
        { id: 101, message: 'Family Deleted Successfully' },
        { id: 102, message: 'Reason Type Added Successfully' },
        { id: 103, message: 'Reason Type Updated Successfully' },
        { id: 104, message: 'Reason Type Deleted Successfully' },
        { id: 105, message: 'Reason Added Successfully' },
        { id: 106, message: 'Reason Updated Successfully' },
        { id: 107, message: 'Reason Deleted Successfully' },
        { id: 108, message: 'Account Group Updated Successfully' },
        { id: 109, message: 'Service Offering Updated Successfully' },
        { id: 110, message: 'Sms Template Added Successfully' },
        { id: 111, message: 'Sms Template Updated Successfully' },
        { id: 112, message: 'Are you sure that you want to delete this sms template ' },
        { id: 113, message: 'Sms Template Deleted Successfully' },
        { id: 114, message: 'Request Parameters Parsed Successfully' },
        { id: 115, message: 'Response Parameters Parsed Successfully' },
        { id: 116, message: 'Are you sure that you want to delete this service offering plan?' },
        { id: 401, message: 'Sorry you have no permissions to view data' },

    ];
    getMessage(id: number) {
        return this.messages.filter((message) => message.id === id)[0];
    }
}
