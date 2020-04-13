import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Bestelling } from '../models/bestelling.model';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { delay, retryWhen } from 'rxjs/operators';

@Injectable()
export class BestellingService {
	constructor(private readonly http: HttpClient) { }

	getNextBestelling(): Observable<Bestelling> {
		return this.http.get<Bestelling>(`${environment.apiUrl}/verwerking/volgendeBestelling`)
			.pipe(
				retryWhen(error => error.pipe(delay(30000)))
			);
	}

    finishBestelling(bestelNummer: number) {
        return this.http.post(`${environment.apiUrl}/verwerking/klaar`, { bestelNummer });
    }

    setArtikelChecked(bestelNummer: number, artikelNummer: number) {
        return this.http.post(`${environment.apiUrl}/verwerking/artikelVerwerkt`, { bestelNummer, artikelNummer });
    }
}
