# 📚 Invoice API Documentation

## 📖 Introduction

The E-Invoice System is a web-based application designed to help users create, manage, and export invoices and quotations digitally. The system simplifies billing operations by allowing users to manage products using CRUD operations and generate invoices that can be downloaded as images or pdf.
This project is suitable for small businesses, freelancers, and students who need a simple and efficient invoicing solution.


## Dear Instructor,
we have created a postman collection for you to test the API endpoints. You can find the collection and environment in the following link: [Invoice Postman Collection](https://drive.google.com/drive/folders/1QjYMpGNVoP7_Zkzo_Fz93ziMHhO5g0IE?usp=sharing). Thank you for your time and consideration 🙏🏻 .


## 📑 Table of Contents

- [📖 Introduction](#-introduction)
- [📑 Table of Contents](#-table-of-contents)
- [⚙️ Installation](#️-installation)
- [🚀 Usage](#-usage)
- [📬 Endpoints](#-endpoints)
  - [👤 User](#-user)
  - [🔐 Auth](#-auth)
  - [🖼️ Media](#-media)
  - [🔍 Quotation](#-quotation)
  - [📂 Invoice](#-invoice)
  - [🎥 Client](#-client)
  - [📜 Product](#-product)
  - [🗂️ Stock](#️-stock)
  - [🔖 Settings](#-settings)
- [📋 Examples](#-examples)
- [🛠️ Troubleshooting](#️-troubleshooting)
- [👥 Contributors](#-contributors)

## ⚙️ Installation

To use the Invoice API, you need to have an active instance of the Inovice service running. Ensure you have the tokens for authentication. You can use tools like Postman to test the API endpoints.

## 🚀 Usage

The Invoice API uses RESTful principles and supports standard HTTP methods such as GET, POST, PUT, PATCH, and DELETE. All endpoints require the base URL ` https://invoice.kimla.online/`, which should be replaced with the actual base URL of your Invoice instance. We will provide you with the Postman Collection and Environment to get started. [Inoive Postman Collection](https://drive.google.com/drive/folders/1QjYMpGNVoP7_Zkzo_Fz93ziMHhO5g0IE?usp=sharing)

## 📬 Endpoints

### 👤 User

| Endpoint          | Method | URL                                     | Description |
|-------------------|--------|-----------------------------------------|-------------|
| Find User Profile | GET    | `/api/v1/users/me`     `                | Retrieves the authenticated user's profile information. |
| Update User       | PATCH  | `/api/v1/users/me`                    | Updates user information based on the provided token |


### 🔐 Auth

| Endpoint              | Method | URL                                      | Description |
|-----------------------|--------|------------------------------------------|-------------|
| Register              | POST   | `/api/v1/auth/register`                  | Registers a new user. |
| Login                 | POST   | `/api/v1/auth/login`                 | Authenticates a user and returns a JWT token. |
| Refresh Token         | POST   | `/api/v1/auth/refresh-token`            | Refreshes the authentication token using a refresh token. |
| Change Password       | POST   | `/api/v1/setting/password`                   | Changes the user's password. |

### 🖼️ Media

| Endpoint     | Method | URL                                    | Description |
|--------------|--------|----------------------------------------|-------------|
| Upload Image | POST   | `api/v1/media/upload-image`                   | Uploads an image file. |

### Quotation

| Endpoint     | Method | URL                                      | Description |
|--------------|--------|------------------------------------------|-------------|
| Create Quotattion      | POST   | `/api/v1/quotations`          | User can create Quotation  |
| Get Quotattion By Id      | GET   | `/api/v1/quotations/{id}`          | User can find quoation by Id   |
| Get All Quotattion      | GET   | `/api/v1/quotations`          | User can find all quoation    |
| Update Quotattion      | PATCH  | `/api/v1/quotations/{id}`          | User can update quoation by id    |
| Delete Quotattion      | DELETE  | `/api/v1/quotations/{id}`          | User can Delete quoation by id    |

### 📂 Invoice

| Endpoint              | Method | URL                                          | Description |
|-----------------------|--------|----------------------------------------------|-------------|
| Create Invoice | POST | `/api/v1/invoices`                     | User can create Invoice |
| Find a Invoice | GET    | `/api/v1/invoices/{id}`     | User can find Invoice by Id  |
| Get all Invoice    | GET | `/api/v1/invoices`                   | Deletes a collection based on the provided UUID. |
| Delete Invoice | DELETE| `/api/v1/invoices/{id}`                       | Delete Invoice by id  |
| Add Item | POST| `/api/v1/invoices/{id}/items`                    |  Add Item for store invoiceId , productId ,unitPrice, quantity, subtotal |
| Delete Item | DELETE | `/api/v1/invoices/{id}/items/{itemId}`                    |  Delete Item invoice |

### Client

| Endpoint     | Method | URL                                                | Description |
|--------------|--------|----------------------------------------------------|-------------|
| Create Client | POST    | `/api/v1/client/create`| Client created for to create Quotation or Invoice  |
| Update Client | PUT    | `/api/v1/client/{id}`| Update info Client  |
| Get My Clients | GET    | `/api/v1/client`| Get my client info Client requrie jwt token  |
| Get Client By Id  | GET    | `/api/v1/client/{id}`| Get client by id  |
| Delete Client By Id  | DELETE    | `/api/v1/client/{id}`| Delete client by id  |


### 📜 Product

| Endpoint     | Method | URL                                                   | Description |
|--------------|--------|-------------------------------------------------------|-------------|
| Create Product  | POST   | `/api/v1/products`                                        | Create product for create quotation or invoice |
| Get All Products | GET| `/api/v1/products` | ... |
| Get My Product | GET| `/api/v1/products/my-products` | ... |
| Delete Product | DELETE | `/api/v1/products/{uuid}` | ... |
| Get Product by uuid | GET | `/api/v1/products/{uuid}` | ... |
| Update Product Product by uuid | PATCH | `/api/v1/products/{uuid}` | ... |
| Create Product Type Product | POST | `/api/v1/products/type` | ... |
| Get My Product Types | GET | `/api/v1/products/type` | ... |

### Stock 

| Endpoint            | Method | URL                                                   | Description                                       |
|---------------------|--------|-------------------------------------------------------|---------------------------------------------------|
| Update Stock      | POST   | `/api/v1/stock/movement`                                             |Records a stock movement (In/Out) and updates current levels.                             |
| Get Movement History       | GET | `/api/v1/stock/movement/{uuid}`                                             | Retrieves a list of all stock movements for a specific product UUID.    |


###  Settings

| Endpoint            | Method | URL                                                        | Description |
|---------------------|--------|------------------------------------------------------------|-------------|
| General Settings    | GET   | `/api/v1/setting`                                                | Retrieves the current user's system settings. |
| General Settings Update  | PATCH    | `/api/v1/setting`    | Updates specific system settings (e.g., currency, theme). |
| Get my User Profile     | GET | `/api/v1/setting/profile`                                                | Fetches the profile details of the currently authenticated user. |
| Update my User Profile     | PATCH    | `/api/v1/setting/profile`                                         | Updates user profile information (name, contact info, etc.). |
| Change password | PATCH  | `/api/v1/setting/password`                                         | Retrieves a bookmark based on the provided UUID. |


## 📋 Examples

For detailed examples on how to use the API endpoints, please refer to the provided Postman collection link: [Inoive Postman Collection](https://drive.google.com/drive/folders/1QjYMpGNVoP7_Zkzo_Fz93ziMHhO5g0IE?usp=sharing)

## 🛠️ Troubleshooting

If you encounter issues while using the Invoice API, consider the following steps:
- Ensure your request URLs are correct and the base URL is properly set.
- Check your tokens for validity and expiration.
- Verify the request body and headers match the expected format.
- Refer to the response messages for specific error details.

## 👥 Contributors

- Invoice Team
- ADITI & Wing Bank 
