import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AccessGuard } from './access-guard';
import { AuthComponent } from './components/auth/auth.component';
import { HomeComponent } from './components/home/home.component';

// add requiresAdmin: true into the data object instead of requiresLogin if admin role required
const routes: Routes = [
  { path: '', component: HomeComponent, data: { requiresLogin: true, redirectTo: '/auth' }, canActivate: [ AccessGuard ] },
  { path: 'auth', component: AuthComponent, data: { requiresLogout: true, redirectTo: '/' }, canActivate: [ AccessGuard ], runGuardsAndResolvers: 'always' },
  { path: 'summary', loadChildren: () => import('../modules/list-view/list-view.module').then(m => m.ListViewModule)},
  { path: 'products', loadChildren: () => import('../modules/product-view/details.module').then(m => m.DetailsModule)},
  { path: 'profile', loadChildren: () => import('../modules/profile/profile.module').then(m => m.ProfileModule)},
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true, onSameUrlNavigation: 'reload' })],
  exports: [RouterModule],
  providers: [AccessGuard]
})
export class AppRoutingModule { }
