import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Bestelling } from '../models/bestelling.model';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

@Injectable()
export class BestellingService {
	constructor(private readonly http: HttpClient) { }

	sendBestelling(bestelling: Bestelling): Observable<any> {
		return this.http.post(environment.apiUrl + '/bestelling', bestelling);
	}

	getBestellingen(): Observable<Bestelling[]> {
		return this.http.get<Bestelling[]>(`${environment.apiUrl}/bestelling`);
	}
}
