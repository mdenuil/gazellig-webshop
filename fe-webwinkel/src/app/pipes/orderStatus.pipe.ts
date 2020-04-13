import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'orderStatus' })
export class OrderStatusPipe implements PipeTransform {
	transform(value: string): string {
		return this.getEnglishStatus(value);
	}

	private getEnglishStatus(status: string): string {
		switch (status) {
			case 'BEHANDELBAAR':
				return 'Ready for collection';
			case 'IN_AFWACHTING':
				return 'Pending stock';
			case 'IN_BEHANDELING':
				return 'Collecting order';
			case 'VERSTUURD':
				return 'Send';
			case 'BETAALD':
				return 'Paid';
			default:
				return '';
		}
	}
}
