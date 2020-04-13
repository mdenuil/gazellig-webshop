import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ValidatorFn, ValidationErrors } from '@angular/forms';
import { AccountService } from '../../services/account.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { first } from 'rxjs/operators';
import { Registreer } from '../../models/registreer.model';

@Component({
	selector: 'app-registreer',
	templateUrl: './register.component.html',
	styleUrls: ['./register.component.scss'],
})
export class RegisterComponent implements OnInit {
	registreerForm: FormGroup;
	loading = false;

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly accountService: AccountService,
		private readonly toastr: ToastrService,
		private readonly router: Router
	) {
		// redirect to home if already logged in
		if (this.accountService.currentUserValue) {
			this.router.navigate(['/']);
		}
	}

	ngOnInit() {
		this.registreerForm = this.formBuilder.group({
			email: [null, [Validators.required, Validators.email]],
			wachtwoord: [null, [Validators.required, Validators.minLength(8)]],
			wachtwoordCheck: [null, [Validators.required, Validators.minLength(8)]],
			initialen: [null, Validators.required],
			achternaam: [null, Validators.required]
		}, {
			validators: [this.passwordsMatchValidator]
		});
	}

	get f() { return this.registreerForm.controls; }

	onSubmit() {
		this.loading = true;

		const registreerGegevens: Registreer = {
			email: this.f.email.value,
			wachtwoord: this.f.wachtwoord.value,
			initialen: this.f.initialen.value,
			achternaam: this.f.achternaam.value,
			klantSoort: ['particulier']
		};

		this.accountService.register(registreerGegevens)
			.pipe(first())
			.subscribe({
				error: (error) => {
					this.loading = false;
				},
				complete: () => {
					this.loading = false;
					this.toastr.info('Account registered', null, {
						timeOut: 6000
					});
					this.router.navigate(['/']);
				}
			});
	}

	passwordsMatchValidator: ValidatorFn = (control: FormGroup): ValidationErrors => {
		const wachtwoord = control.get('wachtwoord');
		const wachtwoordCheck = control.get('wachtwoordCheck');

		if (wachtwoord.valid && wachtwoordCheck.valid) {
			return wachtwoord.value === wachtwoordCheck.value ? null : { 'passwordsMatch': true };
		} else {
			return null;
		}
	};
}
