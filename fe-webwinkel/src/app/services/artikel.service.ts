import { Injectable } from '@angular/core';
import { Artikel } from '../models/artikel.model';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable()
export class ArtikelService {
	private readonly artikelenSubject: Subject<Artikel[]> = new Subject();
	private artikelen: Artikel[] = [];

	constructor(private readonly http: HttpClient) { }

	getArtikelen(filter?: string): Observable<Artikel[]> {
		if (filter) {
			this.http.get<Artikel[]>(`${environment.apiUrl}/artikel/filter?key=${filter}`)
				.subscribe({
					next: artikelen => {
						this.artikelen = artikelen;
						this.artikelenSubject.next(this.artikelen);
					},
					error: (error) => this.artikelenSubject.error(error)
				});
		} else {
			this.http.get<Artikel[]>(`${environment.apiUrl}/artikel`)
				.subscribe({
					next: artikelen => {
						this.artikelen = artikelen;
						this.artikelenSubject.next(this.artikelen);
					},
					error: (error) => this.artikelenSubject.error(error)
				});
		}

		return this.artikelenSubject;
	}

	getArtikel(artikelNummer: number): Observable<Artikel> {
		return this.http.get<Artikel>(`${environment.apiUrl}/artikel/${artikelNummer}`);
	}
}
