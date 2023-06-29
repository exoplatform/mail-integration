# mail-integration
Mail Integration Add-on



## Configure Integration with Google Mail
To use this addon with a gmail account, some actions are required :
- Activate IMAP on gmail account : https://mail.google.com/mail/u/0/#settings/fwdandpop
- Use an application password. To use that, you need to activate 2-Factor Authentication on Google account :

  - https://myaccount.google.com/security : check that 2FA is activated
  - Then create a password application https://myaccount.google.com/apppasswords : generate and copy the password. This is this one to use in the drawer
- Other parameters in the drawer are :
  - Url : imap.gmail.com
  - Port : 993
  - Login : your email address
  - Password : the application password just generated
