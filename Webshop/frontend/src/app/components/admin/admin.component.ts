import { Component, OnInit } from "@angular/core";
import { FormControl, FormGroup } from "@angular/forms";
import { UserDetailsResponse } from "src/app/api/webshop/models";
import { AdminService } from "src/app/api/webshop/services";

@Component({
    selector: 'admin',
    templateUrl: './admin.component.html',
    styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit {

    constructor (private readonly adminService: AdminService) {}

    users: UserDetailsResponse[] = [];

    userSelected: boolean = false;
    isAdmin: boolean = false;

    selectedUser: UserDetailsResponse = {
        id: 0,
        name: "",
        username: "",
        email: "",
        roles: []
    }

    formGroup = new FormGroup({
        adminCheckbox: new FormControl()
    })


    ngOnInit(): void {
        this.adminService.getAdminUsers().subscribe({
            next: (users) => {
                this.users = users;
            },
            error: err => window.alert(err.error),
        })
    }

    selectUser(user: UserDetailsResponse) {
        console.log(user);
        this.userSelected = true;
        this.selectedUser = user;

        if (user.roles.includes("ROLE_ADMIN")) {
            this.isAdmin = true;
            console.log(this.isAdmin);
        }
        else {
            this.isAdmin = false;
            console.log(this.isAdmin);
        }
    }

    closeUser() {
        this.userSelected = false;
        let adminChecked = this.formGroup.value.adminCheckbox;

        console.log(adminChecked);
    }

    
}