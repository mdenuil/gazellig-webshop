import { ShoppingCartService } from './shopping-cart.service';
import { Artikel } from '../models/artikel.model';
import { TestBed } from '@angular/core/testing';
import { ArtikelService } from './artikel.service';
import { HttpClientModule } from '@angular/common/http';
import { BesteldArtikel } from '../models/besteldArtikel.model';
import { AccountService } from './account.service';

describe('ShoppingCartService', () => {
    let shoppingCartService: ShoppingCartService;
    let artikelen: BesteldArtikel[];
    const artikel: Artikel = {
        artikelNummer: 8,
        aantal: 23,
        prijs: 8.00,
        naam: 'AWC Logo Cap',
        beschrijving: 'Traditional style with a flip-up brim; one-size fits all.',
        afbeeldingUrl: 'no_image_available_small.gif',
        leverbaarVanaf: '1998-06-01T00:00:00',
        leverbaarTot: null,
        leverancierCode: 'CA-1098',
        leverancier: 'Batavus',
        categorieen: [
            'Clothing',
            'Caps'
        ]
    };

    beforeEach(() => {
        artikelen = [
            {
                artikelNummer: 3,
                aantal: 1,
                artikel
            },
            {
                artikelNummer: 8,
                aantal: 1,
                artikel
            }
        ];

        localStorage.shoppingCart = JSON.stringify(artikelen);

        TestBed.configureTestingModule({
            imports: [HttpClientModule],
            providers: [
                ArtikelService,
                ShoppingCartService,
                AccountService
            ]
        });
        shoppingCartService = TestBed.get(ShoppingCartService);
    });

    it('should load localstorage item when starting', () => {
        expect(localStorage.shoppingCart).toBe(JSON.stringify(artikelen));
    });

    it('should update localstorage item when an artikel is added', () => {
        const newBesteldArtikel: BesteldArtikel = {
            artikelNummer: 8,
            aantal: 1,
            artikel
        };
        artikelen.push(newBesteldArtikel);

        shoppingCartService.addArtikel(newBesteldArtikel);

        expect(localStorage.shoppingCart).toBe(JSON.stringify(artikelen));
    });
});
