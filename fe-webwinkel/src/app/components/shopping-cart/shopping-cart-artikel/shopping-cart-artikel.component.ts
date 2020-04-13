import { Component, Input } from '@angular/core';
import { BesteldArtikel } from '../../../models/besteldArtikel.model';
import { ShoppingCartService } from '../../../services/shopping-cart.service';

@Component({
    selector: 'app-shopping-cart-artikel',
    templateUrl: './shopping-cart-artikel.component.html',
    styleUrls: ['./shopping-cart-artikel.component.scss']
})
export class ShoppingCartArtikelComponent {
    @Input() besteldArtikel: BesteldArtikel;

    constructor(private readonly shoppingcartService: ShoppingCartService) { }

    subtractAmount() {
        if (this.besteldArtikel.aantal > 1) {
            this.besteldArtikel.aantal--;
            this.shoppingcartService.updateArtikel(this.besteldArtikel);
        }
    }

    addAmount() {
        this.besteldArtikel.aantal++;
        this.shoppingcartService.updateArtikel(this.besteldArtikel);
    }

    delete() {
        this.shoppingcartService.deleteArtikel(this.besteldArtikel);
    }
}
