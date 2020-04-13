import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Login } from '../models/login.model';
import { Observable, BehaviorSubject } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { Gebruiker } from '../models/gebruiker.model';
import { Registreer } from '../models/registreer.model';

@Injectable({
	providedIn: 'root'
})
export class AccountService {
	private readonly currentUserSubject: BehaviorSubject<Gebruiker>;
	public currentUser: Observable<Gebruiker>;

	constructor(private readonly http: HttpClient) {
		this.currentUserSubject = new BehaviorSubject<Gebruiker>(JSON.parse(localStorage.getItem('currentUser')));
		this.currentUser = this.currentUserSubject.asObservable();
	}

	public get currentUserValue(): Gebruiker {
		return this.currentUserSubject.value;
	}

	public get isLoggedIn(): boolean {
		return this.currentUserSubject.value !== null;
	}

	login(logingegevens: Login): Observable<any> {
		return this.http.post<Gebruiker>(environment.apiUrl + '/auth/login', logingegevens)
			.pipe(map(user => {
				// store user details and jwt token in local storage to keep user logged in between page refreshes
				localStorage.setItem('currentUser', JSON.stringify(user));
				this.currentUserSubject.next(user);

				this.http.get<Gebruiker>(`${environment.apiUrl}/klant/ik`).subscribe(klant => {
					klant.token = user.token;
					localStorage.setItem('currentUser', JSON.stringify(klant));
					this.currentUserSubject.next(klant);
					return user;
				});
			}));
	}

	logout() {
		// remove user from local storage and set current user to null
		localStorage.removeItem('currentUser');
		this.currentUserSubject.next(null);
	}

	register(registreerGegevens: Registreer): Observable<any> {
		return this.http.post<any>(environment.apiUrl + '/auth/registreer', registreerGegevens)
	}
}
