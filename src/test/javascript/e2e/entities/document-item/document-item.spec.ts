import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { DocumentItemComponentsPage, DocumentItemDeleteDialog, DocumentItemUpdatePage } from './document-item.page-object';

const expect = chai.expect;

describe('DocumentItem e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let documentItemComponentsPage: DocumentItemComponentsPage;
  let documentItemUpdatePage: DocumentItemUpdatePage;
  let documentItemDeleteDialog: DocumentItemDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load DocumentItems', async () => {
    await navBarPage.goToEntity('document-item');
    documentItemComponentsPage = new DocumentItemComponentsPage();
    await browser.wait(ec.visibilityOf(documentItemComponentsPage.title), 5000);
    expect(await documentItemComponentsPage.getTitle()).to.eq('scriptumBasikApp.documentItem.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(documentItemComponentsPage.entities), ec.visibilityOf(documentItemComponentsPage.noResult)),
      1000
    );
  });

  it('should load create DocumentItem page', async () => {
    await documentItemComponentsPage.clickOnCreateButton();
    documentItemUpdatePage = new DocumentItemUpdatePage();
    expect(await documentItemUpdatePage.getPageTitle()).to.eq('scriptumBasikApp.documentItem.home.createOrEditLabel');
    await documentItemUpdatePage.cancel();
  });

  it('should create and save DocumentItems', async () => {
    const nbButtonsBeforeCreate = await documentItemComponentsPage.countDeleteButtons();

    await documentItemComponentsPage.clickOnCreateButton();

    await promise.all([
      documentItemUpdatePage.setNameInput('name'),
      documentItemUpdatePage.setGoogleDocsIdInput('googleDocsId'),
      documentItemUpdatePage.setDocumentTextInput('documentText'),
      documentItemUpdatePage.setCreatedAtInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      documentItemUpdatePage.setUpdatedAtInput('01/01/2001' + protractor.Key.TAB + '02:30AM')
    ]);

    expect(await documentItemUpdatePage.getNameInput()).to.eq('name', 'Expected Name value to be equals to name');
    expect(await documentItemUpdatePage.getGoogleDocsIdInput()).to.eq(
      'googleDocsId',
      'Expected GoogleDocsId value to be equals to googleDocsId'
    );
    expect(await documentItemUpdatePage.getDocumentTextInput()).to.eq(
      'documentText',
      'Expected DocumentText value to be equals to documentText'
    );
    expect(await documentItemUpdatePage.getCreatedAtInput()).to.contain(
      '2001-01-01T02:30',
      'Expected createdAt value to be equals to 2000-12-31'
    );
    expect(await documentItemUpdatePage.getUpdatedAtInput()).to.contain(
      '2001-01-01T02:30',
      'Expected updatedAt value to be equals to 2000-12-31'
    );

    await documentItemUpdatePage.save();
    expect(await documentItemUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await documentItemComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last DocumentItem', async () => {
    const nbButtonsBeforeDelete = await documentItemComponentsPage.countDeleteButtons();
    await documentItemComponentsPage.clickOnLastDeleteButton();

    documentItemDeleteDialog = new DocumentItemDeleteDialog();
    expect(await documentItemDeleteDialog.getDialogTitle()).to.eq('scriptumBasikApp.documentItem.delete.question');
    await documentItemDeleteDialog.clickOnConfirmButton();

    expect(await documentItemComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
