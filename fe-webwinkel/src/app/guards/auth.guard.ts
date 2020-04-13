import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { AccountService } from '../services/account.service';

@Injectable()
export class AuthGuard implements CanActivate {
	constructor(
		private readonly accountService: AccountService,
		private readonly router: Router
	) { }

	canActivate(
		route: ActivatedRouteSnapshot,
		state: RouterStateSnapshot
	): Observable<boolean> | Promise<boolean> | boolean {
		if (!this.accountService.isLoggedIn) {
			this.router.navigate(['/login']);
		}
		return this.accountService.isLoggedIn;
	}
}
