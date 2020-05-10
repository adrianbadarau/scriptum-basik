import { element, by, ElementFinder } from 'protractor';

export class DocumentItemComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-document-item div table .btn-danger'));
  title = element.all(by.css('jhi-document-item div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class DocumentItemUpdatePage {
  pageTitle = element(by.id('jhi-document-item-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  nameInput = element(by.id('field_name'));
  googleDocsIdInput = element(by.id('field_googleDocsId'));
  documentTextInput = element(by.id('field_documentText'));
  createdAtInput = element(by.id('field_createdAt'));
  updatedAtInput = element(by.id('field_updatedAt'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setNameInput(name: string): Promise<void> {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput(): Promise<string> {
    return await this.nameInput.getAttribute('value');
  }

  async setGoogleDocsIdInput(googleDocsId: string): Promise<void> {
    await this.googleDocsIdInput.sendKeys(googleDocsId);
  }

  async getGoogleDocsIdInput(): Promise<string> {
    return await this.googleDocsIdInput.getAttribute('value');
  }

  async setDocumentTextInput(documentText: string): Promise<void> {
    await this.documentTextInput.sendKeys(documentText);
  }

  async getDocumentTextInput(): Promise<string> {
    return await this.documentTextInput.getAttribute('value');
  }

  async setCreatedAtInput(createdAt: string): Promise<void> {
    await this.createdAtInput.sendKeys(createdAt);
  }

  async getCreatedAtInput(): Promise<string> {
    return await this.createdAtInput.getAttribute('value');
  }

  async setUpdatedAtInput(updatedAt: string): Promise<void> {
    await this.updatedAtInput.sendKeys(updatedAt);
  }

  async getUpdatedAtInput(): Promise<string> {
    return await this.updatedAtInput.getAttribute('value');
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class DocumentItemDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-documentItem-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-documentItem'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
