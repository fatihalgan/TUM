<?xml version='1.0' encoding='UTF-8'?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0">
  <jsp:output omit-xml-declaration="true" doctype-root-element="HTML"
              doctype-system="http://www.w3.org/TR/html4/loose.dtd"
              doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN"/>
  <jsp:directive.page contentType="text/html;charset=UTF-8"/>
  <html>
  <head>
     <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
     <TITLE>:: mCel Topup Mediation Manager ::</TITLE>
     <style>
         body {
         font-family: tahoma;
         font-size: 12px;
         color: #333333;
         background-color: #cccccc;
         background-image: url(/TUMManager/images/loginbg.gif);
         background-position: right bottom;
         background-repeat: no-repeat;
         }
         .notice {
         font-size: 10px;
         color: #999999;
         }
         a {
         font-size: 10px;
         font-family: tahoma;
         color: #999999;
         text-decoration: none;
         }
         a:hover {
         font-size: 10px;
         font-family: tahoma;
         color: #ffffff;
         text-decoration: none;
         }
         input {
         font-family: courier new;
         font-size: 12px;
         font-weight: bold;
         color: #d92c00;
         background-color: #fdf1c2;
         border: 1px solid #999999;
         }
         .dugme {
         font-family: tahoma;
         font-size: 10px;
         font-weight: normal;
         color: #ffffff;
         background-color: #999999;
         border: 1px solid #999999;
         cursor: hand;
         }
         .dugme:hover {
         font-family: tahoma;
         font-size: 10px;
         color: #ffffff;
         background-color: #ee5528;
         border: 1px solid #999999;
         cursor: hand;
         }
         
     </style>
  </head>
  <body marginheight="0" marginwidth="0" leftmargin="0" topmargin="0" onload="document.getElementById('usr').focus();">
    <form action="j_acegi_security_check" method="post" name="LoginForm">
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
          <tr>
              <td height="6" valign="top" style="height: 6px; background-image: url(/TUMManager/images/bgtop.gif); background-position: top left; background-repeat: repeat-x;"></td>
          </tr>
          <tr>
              <td valign="top" align="center">
                  <table border="0" width="250" align="center">
                      <tr>
                          <td colspan="2" height="200"></td>
                      </tr>
                      <tr>
                          <td colspan="2">
                              <table border="0" cellpadding="0" cellspacing="0" align="left">
                                  <tr>
                                      <td valign="middle" width="20"><img src="/TUMManager/images/loginicon.gif"/></td>
                                      <td valign="middle" style="font-family: Trebuchet MS; font-size: 17px">User Login</td>
                                  </tr>
                              </table>  
                          </td>
                      </tr>
                      <tr>
                          <td colspan="2" width="250" align="center">
                            <table cellspacing="5" border="0" width="250" height="100" bgcolor="#eeeeee" style="border: 1px dashed #999999">
                              <tr>
                                  <td align="left" valign="middle" width="100" style="font-family: Trebuchet MS">User Name:</td>
                                  <td align="right" valign="middle" width="150"><input name="j_username" type="text" id="usr"/></td>
                              </tr>
                              <tr>
                                  <td align="left" valign="middle" width="100" style="font-family: Trebuchet MS">Password</td>
                                  <td align="right" valign="middle" width="150"><input name="j_password" type="password"/></td>
                              </tr>
                              <tr>
                                  <td align="left" valign="middle" width="100"></td>
                                  <td align="right" valign="middle" width="150"><input class="dugme" type="submit" name="action" value="Login"/></td>
                              </tr>
                            </table>
                          </td>
                      </tr>
                      <tr>
                          <td align="left" valign="top" class="notice">(c) 2007 <a href="http://www.mcel.co.mz" target="_blank">MCel</a></td>
                      </tr>
                  </table>
              </td>
          </tr>    
      </table>
    </form>
  </body>
  </html>
</jsp:root>