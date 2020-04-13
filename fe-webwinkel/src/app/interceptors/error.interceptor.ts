import {
	HttpInterceptor,
	HttpHandler,
	HttpRequest,
	HttpEvent,
	HttpErrorResponse,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ToastrService } from 'ngx-toastr';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
	constructor(public toasterService: ToastrService) { }

	intercept(
		req: HttpRequest<any>,
		next: HttpHandler
	): Observable<HttpEvent<any>> {
		return next.handle(req).pipe(
			catchError((error: any) => {
				if (error instanceof HttpErrorResponse) {
					const message = this.getHttpErrorMessage(error.status);
					this.toasterService.error(message, null, {
						timeOut: 6000,
					});
				}

				return throwError(error);
			})
		);
	}

	private getHttpErrorMessage(errorStatus: number): string {
		switch (errorStatus) {
			case 401:
			case 403:
				return 'Wrong credentials';
			case 404:
				return 'Page not found';
			default:
				return 'Something went wrong, please try again later';
		}
	}
}
