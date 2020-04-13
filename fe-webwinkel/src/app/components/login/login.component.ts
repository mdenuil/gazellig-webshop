import { Component, OnInit, Input } from '@angular/core';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { Login } from '../../models/login.model';
import { AccountService } from '../../services/account.service';
import { first } from 'rxjs/operators';
import { Router } from '@angular/router';

@Component({
	selector: 'app-login',
	templateUrl: './login.component.html',
	styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
	@Input() successRedirectUrl = "/";
	loginForm: FormGroup;
	loading = false;

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly accountService: AccountService,
		private readonly toastr: ToastrService,
		private readonly router: Router,
	) { }

	ngOnInit() {
		// redirect to home if already logged in
		if (this.accountService.currentUserValue) {
			this.router.navigate(['/']);
		}

		this.loginForm = this.formBuilder.group({
			email: [null, [Validators.required, Validators.email]],
			wachtwoord: [null, [Validators.required, Validators.minLength(8)]]
		});
	}

	// convenience getter for easy access to form fields
	get f() { return this.loginForm.controls; }

	onSubmit() {
		this.loading = true;

		const loginGegevens: Login = {
			email: this.f.email.value,
			wachtwoord: this.f.wachtwoord.value
		};

		this.accountService.login(loginGegevens)
			.pipe(first())
			.subscribe({
				next: () => {
					this.router.navigate([this.successRedirectUrl]);
				},
				error: () => {
					this.loading = false;
				},
				complete: () => {
					this.loading = false;
				}
			});
	}
}
