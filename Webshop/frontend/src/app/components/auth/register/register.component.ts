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
