import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'inStock' })
export class InStockPipe implements PipeTransform {
	transform(value: number): any {
		return value > 10 ? "10+" : value;
	}
}
