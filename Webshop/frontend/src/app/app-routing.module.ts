import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AccessGuard } from './access-guard';
import { AuthComponent } from './components/auth/auth.component';
import { RegisterComponent } from './components/auth/register/register.component';
import { HomeComponent } from './components/home/home.component';
import { FileUploadComponent } from './components/upload/file-upload.component';

// add requiresAdmin: true into the data object instead of requiresLogin if admin role required
const routes: Routes = [
  { path: '', component: HomeComponent, data: { requiresLogin: true, redirectTo: '/auth' }, canActivate: [ AccessGuard ] },
  { path: 'auth', component: AuthComponent, data: { requiresLogout: true, redirectTo: '/' }, canActivate: [ AccessGuard ], runGuardsAndResolvers: 'always' },
  { path: 'register', component: RegisterComponent, data: {reguiresLogout: true, redirectTo: '/'}, canActivate: [AccessGuard], runGuardsAndResolvers: 'always'},
  
  //FileUploadCompoenent and cartComponent: requiresLogin should be true, it's off just for development
  { path: 'file-upload', component: FileUploadComponent, data: {requiresLogin: false, redirectTo: '/auth'}, canActivate: [AccessGuard]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true, onSameUrlNavigation: 'reload' })],
  exports: [RouterModule],
  providers: [AccessGuard]
})
export class AppRoutingModule { }
