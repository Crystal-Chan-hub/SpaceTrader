# SpaceTraderApp User Guide
#### This project is developed using [Space Trader API](https://spacetraders.io/).
## To run:
- To launch, use command `gradle run --args="[online/offline]"`, use either online or offline in the brackets with to control if this app will use online API.
- eg. `gradle run --args="offline"`
## App launch - Initial page:
- You can select from two buttons to create new spacetrader account or login with existing account.
## Create Account:
You need to input username wanted to create a new account
You can press ***Back*** button to go back to the initial page.
## Login with account:
1. You need to input username to text box described as ***Username***
2. Input token to text box described as ***Token***
3. Press ***Login*** button to login
- You can press ***Back*** button to go back to the initial page.
## Main page:
Once you logged in or created new account, you are able to copy your token from the text box described as ***Save token for login*** to save it on your device for later login.
Game status is shown at the bottom of the window.

### Left buttons are user related buttons:
- ***User Info*** button: This button will navigate you to a new Page. You are able to view your personal information in this page.You can press ***Back*** button to go back to the Main page.

- ***Your Loans*** button: This button will navigate you to a new Page. You are able to view your loans information in this page. If you have not request for any loans, page will display related message. If you have active loans, they will be able to pay off loans in this page. Loans ids will be displayed in text box described as ***Your loans ids are listed below*** for you to copy. You need to input the loans id that they wish to pay off in the given text box described as ***Loan id to pay off*** and press ***Pay off*** button to pay off that loan.You can press ***Back*** button to go back to the Main page.
     - ***Pay off*** button: If you have successfully paid off a loan, this will navigate you to a new page with updated user info. If you have not succeed, a warning window will pop out with related message. You will need to close the warning window to do further steps.

- ***Your Ships*** button: This button will navigate you to a new Page. You are able to view your ships information in this page.You can press ***Back*** button to go back to the Main page.

- ***Nearby Locations*** button: This button will navigate you to a new Page. You are able to view the nearby locations in the same system in this page. You can press ***Back*** button to go back to the Main page.

### Center buttons are requests related buttons:
- ***Request Loans*** button: This button will navigate you to a new Page. You can view available loans and obtain loans from this page. You can input the loan type that you wish to obtain in the text box described as ***Loans type***. You can then press the ***Request*** button to obtain the loan you wished. You can press ***Back*** button to go back to the Main page.
     - ***Request*** button: If you have successfully obtained a loan, this will navigate you to a new page with succeed loans info. You can press ***Back*** button to go back to the Main page. If you have not succeed, a warning window will pop out with related message. You will need to close the warning window to do further steps.

- ***Buy Ships*** button: This button will navigate you to a new Page. You can view available ships and buy ships from this page. You can input the location you want to buy a ships from in the text box described as ***Location to buy*** and you also need to input the ship type you wish to buy in the text box described as ***Ship Type***. Then, you can press the ***Buy Ship*** button to buy the ship that you wished. You can press ***Back*** button to go back to the Main page.
     - ***Buy Ship*** button: If you have successfully purchased a ship, this will navigate you to a new page with succeed purchased ship info. You can press ***Back*** button to go back to the Main page. If you have not succeed, a warning window will pop out with related message. You will need to close the warning window to do further steps.

- ***Flight Plan*** button: This button will navigate you to a new Page. You can create a new flight plan in this page. Your can copy your ships ids that are listed in text box described as ***Your ships' ids are listed below***. You need to input the ship id that you wish to fly with in the text box described as ***Ship id***. You also need to input the destination that you wish to fly to in the text box described as ***Destination*** and you can press the ***Create Plan*** button to create a new flight plan with the input that you give. You can press ***Back*** button to go back to the Main page.
     - ***Create Plan*** button: If you have successfully created a flight plan, this will navigate you to a new page with current flight plan. You can press ***Back*** button to go back to the Main page. If you have not succeed, a warning window will pop out with related message. You will need to close the warning window to do further steps. You can press ***Back*** button to go back to the Main page.

     - ***View current flight plan*** button: If you have current flight plan, this will navigate you to a new Page with your current flight plan info. You can press ***Back*** button to go back to the Main page. You can press ***Refresh current flight plan*** button to manually refresh the current flight plan. If you have no current flight plan, a warning window will pop out with related message. You will need to close the warning window to do further steps.

### Right buttons are marketplace related buttons:
- ***Marketplace*** button: You view the marketplace details of a location by inputting the location that you want view in the text box described as ***Location***. Then, you can press the ***Get*** button to get the marketplace info for that location. You can press ***Back*** button to go back to the Main page.
     - ***Get*** button: If the get of marketplace info of this location succeed, this will navigate you to a new page with info of the marketplace in the location you have input. You can press ***Back*** button to go back to the Main page. If you the get failed, a warning window will pop out with related message. You will need to close the warning window to do further steps.

- ***Buy Fuel*** button: This button will navigate you to a new Page. You can buy fuel for your ships in this page. Your can copy your ships ids that are listed in text box described as ***Your ships' ids are listed below***. You need to input the ship id that you wish to buy for with in the text box described as ***Ship id***. You also need to input the quantity of fuel as an integer that you wish to buy to the text box described as ***Quantity*** and you can press the ***Purchase*** button to buy fuel for your given ship. You can press ***Back*** button to go back to the Main page.
     - ***Purchase*** button: If you have successfully purchased fuel, this will navigate you to a new page with your order and updated ship info. You can press ***Back*** button to go back to the Main page. If you have not succeed, a warning window will pop out with related message. You will need to close the warning window to do further steps.

- ***Trade Good*** button: This button will navigate you to a new Page. You can buy or sell good for or on your ships in this page. Your can copy your ships ids that are listed in text box described as ***Your ships' ids are listed below***. You need to input the ship id that you wish to buy for with in the text box described as ***Ship id***. You then need to input the good that you wish to buy or sell in the text box described as ***Good***. You also need to input the quantity of that good as an integer that you wish to buy or sell in the text box described as ***Quantity*** and you can press the ***Purchase*** button to buy or sell the good for your given ship. You can press ***Back*** button to go back to the Main page.
     - ***Purchase*** button: If you have successfully purchased good, this will navigate you to a new page with your order and updated ship info. You can press ***Back*** button to go back to the Main page. If you have not succeed, a warning window will pop out with related message. You will need to close the warning window to do further steps.

     - ***Sell*** button: If you have successfully sold good, this will navigate you to a new page with your order and updated ship info. You can press ***Back*** button to go back to the Main page. If you have not succeed, a warning window will pop out with related message. You will need to close the warning window to do further steps.
