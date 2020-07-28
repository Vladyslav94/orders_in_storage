1. Clone the project to your local IDEA using git clone https://github.com/Vladyslav94/orders_in_storage.git
2. Create SQL connection (using name and password from .properties file or setting your own) and DB order_storage
3. Run the App.
4. Use endpoint /orders/create?price=price&quantity=quantity&item=itemName to create an item
5. Use endpoint /orders/getAllOrders?item=itemName to get the list of item by the lowest price (default restricted by 10 items)
6. To set custom restriction use endpoint /orders/getAllOrders?item=itemName&pageNo=pageNumber&pageSize=numberOfItemsToBeDisplayed
