import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  name: string = '';
  username: string = '';
  email: string = '';
  password: string = '';
  confirmPassword: string = '';

  emailPattern = "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$";
  minNameLength = 4;
  minPwdLength = 8;

  constructor(
    private readonly userService: UserService,
  ) { }

  ngOnInit(): void {
  }

  onRegister() {
     this.userService.register(this.name, this.username, this.email, this.password, this.confirmPassword).subscribe({
       next: () => {
           this.name = '';
           this.username = '';
           this.email = '';
           this.password = '';
           this.confirmPassword = '';
         window.alert('Successful registration');
       },
       error: err => window.alert(err.error),
     });
  }

}
