import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IDocumentItem, DocumentItem } from 'app/shared/model/document-item.model';
import { DocumentItemService } from './document-item.service';
import { DocumentItemComponent } from './document-item.component';
import { DocumentItemDetailComponent } from './document-item-detail.component';
import { DocumentItemUpdateComponent } from './document-item-update.component';

@Injectable({ providedIn: 'root' })
export class DocumentItemResolve implements Resolve<IDocumentItem> {
  constructor(private service: DocumentItemService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDocumentItem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((documentItem: HttpResponse<DocumentItem>) => {
          if (documentItem.body) {
            return of(documentItem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DocumentItem());
  }
}

export const documentItemRoute: Routes = [
  {
    path: '',
    component: DocumentItemComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'scriptumBasikApp.documentItem.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: DocumentItemDetailComponent,
    resolve: {
      documentItem: DocumentItemResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'scriptumBasikApp.documentItem.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: DocumentItemUpdateComponent,
    resolve: {
      documentItem: DocumentItemResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'scriptumBasikApp.documentItem.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: DocumentItemUpdateComponent,
    resolve: {
      documentItem: DocumentItemResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'scriptumBasikApp.documentItem.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
