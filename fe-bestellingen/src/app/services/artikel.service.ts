import { Injectable } from '@angular/core';
import { Artikel } from '../models/artikel.model';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

@Injectable()
export class ArtikelService {
	constructor(private readonly http: HttpClient) { }

	getArtikel(artikelNummer: number): Observable<Artikel> {
		return this.http.get<Artikel>(`${environment.apiUrl}/artikel/${artikelNummer}`);
	}
}
