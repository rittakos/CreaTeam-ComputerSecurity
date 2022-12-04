import { Component, OnInit } from "@angular/core";
import { FormControl, FormGroup } from "@angular/forms";
import { ModifyUserRequest, UserDetailsResponse } from "src/app/api/webshop/models";
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
    adminChecked: string = "false";
    isUser: boolean = true;


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
        this.loadUsers();
    }

    loadUsers() {
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
            this.adminChecked = "true";
            this.isUser = false;
        }
        else {
            this.isAdmin = false;
            this.adminChecked = "false";
            this.isUser = true;
        }
    }

    onRoleChange(event: any) {
        this.adminChecked = event.target.value;
    }

    closeUser() {
        this.userSelected = false;

        console.log("is admin checked " + this.adminChecked);

        console.log("length " + this.selectedUser.roles.length );

        if (this.adminChecked == "true") {
            if (this.selectedUser.roles.includes("ROLE_ADMIN")) {
                console.log("Already admin, roles: " + this.selectedUser.roles);
            }


            if (this.selectedUser.roles.length == 1){
                this.selectedUser.roles.push("ROLE_ADMIN");

                console.log("Became admin, roles: " + this.selectedUser.roles);
            }
        }

        if (this.adminChecked == "false") {
            if (this.selectedUser.roles.includes("ROLE_ADMIN")) {
                this.selectedUser.roles.pop();
                this.selectedUser.roles.pop();
 
                this.selectedUser.roles.push("ROLE_USER");
                
                console.log("User's roles: " + this.selectedUser.roles);
            }
            else {
                console.log("Wasn't admin before, roles: " + this.selectedUser.roles);
            }
        }

        let modifyUser: ModifyUserRequest = {
            email: this.selectedUser.email,
            name: this.selectedUser.name,
            roles: this.selectedUser.roles
        }

        this.adminService.putAdminModifyUser({id: this.selectedUser.id, body: modifyUser}).subscribe({
            next: () => {
              this.loadUsers();
            },
            error: err => window.alert(err.error),
        })

        
    }

    
}