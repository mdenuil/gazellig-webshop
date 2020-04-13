import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ProcessComponent } from './components/process/process.component';

const routes: Routes = [
	{ path: '', component: ProcessComponent }
];
@NgModule({
	imports: [RouterModule.forRoot(routes)],
	exports: [RouterModule]
})
export class AppRoutingModule { }
