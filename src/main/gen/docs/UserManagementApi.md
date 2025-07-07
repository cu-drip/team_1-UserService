# UserManagementApi

All URIs are relative to *https://api.user.local*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**usersGet**](UserManagementApi.md#usersGet) | **GET** /users | Получить список пользователей |
| [**usersIdAvatarPatch**](UserManagementApi.md#usersIdAvatarPatch) | **PATCH** /users/{id}/avatar | Обновить аватар пользователя |
| [**usersIdGet**](UserManagementApi.md#usersIdGet) | **GET** /users/{id} | Получить пользователя по ID |
| [**usersIdPatch**](UserManagementApi.md#usersIdPatch) | **PATCH** /users/{id} | Обновить профиль пользователя |
| [**usersPost**](UserManagementApi.md#usersPost) | **POST** /users | Создать пользователя |


<a id="usersGet"></a>
# **usersGet**
> List&lt;User&gt; usersGet()

Получить список пользователей

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.UserManagementApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://api.user.local");

    UserManagementApi apiInstance = new UserManagementApi(defaultClient);
    try {
      List<User> result = apiInstance.usersGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UserManagementApi#usersGet");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;User&gt;**](User.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Список пользователей |  -  |

<a id="usersIdAvatarPatch"></a>
# **usersIdAvatarPatch**
> User usersIdAvatarPatch(id, usersIdAvatarPatchRequest)

Обновить аватар пользователя

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.UserManagementApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://api.user.local");

    UserManagementApi apiInstance = new UserManagementApi(defaultClient);
    UUID id = UUID.randomUUID(); // UUID | Идентификатор пользователя
    UsersIdAvatarPatchRequest usersIdAvatarPatchRequest = new UsersIdAvatarPatchRequest(); // UsersIdAvatarPatchRequest | 
    try {
      User result = apiInstance.usersIdAvatarPatch(id, usersIdAvatarPatchRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UserManagementApi#usersIdAvatarPatch");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **UUID**| Идентификатор пользователя | |
| **usersIdAvatarPatchRequest** | [**UsersIdAvatarPatchRequest**](UsersIdAvatarPatchRequest.md)|  | |

### Return type

[**User**](User.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Аватар обновлён |  -  |

<a id="usersIdGet"></a>
# **usersIdGet**
> User usersIdGet(id)

Получить пользователя по ID

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.UserManagementApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://api.user.local");

    UserManagementApi apiInstance = new UserManagementApi(defaultClient);
    UUID id = UUID.randomUUID(); // UUID | Идентификатор пользователя
    try {
      User result = apiInstance.usersIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UserManagementApi#usersIdGet");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **UUID**| Идентификатор пользователя | |

### Return type

[**User**](User.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Пользователь найден |  -  |
| **404** | Пользователь не найден |  -  |

<a id="usersIdPatch"></a>
# **usersIdPatch**
> User usersIdPatch(id, userUpdateRequest)

Обновить профиль пользователя

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.UserManagementApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://api.user.local");

    UserManagementApi apiInstance = new UserManagementApi(defaultClient);
    UUID id = UUID.randomUUID(); // UUID | Идентификатор пользователя
    UserUpdateRequest userUpdateRequest = new UserUpdateRequest(); // UserUpdateRequest | 
    try {
      User result = apiInstance.usersIdPatch(id, userUpdateRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UserManagementApi#usersIdPatch");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **UUID**| Идентификатор пользователя | |
| **userUpdateRequest** | [**UserUpdateRequest**](UserUpdateRequest.md)|  | |

### Return type

[**User**](User.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Профиль обновлён |  -  |

<a id="usersPost"></a>
# **usersPost**
> User usersPost(userCreateRequest)

Создать пользователя

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.UserManagementApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://api.user.local");

    UserManagementApi apiInstance = new UserManagementApi(defaultClient);
    UserCreateRequest userCreateRequest = new UserCreateRequest(); // UserCreateRequest | 
    try {
      User result = apiInstance.usersPost(userCreateRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UserManagementApi#usersPost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **userCreateRequest** | [**UserCreateRequest**](UserCreateRequest.md)|  | |

### Return type

[**User**](User.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Пользователь создан |  -  |

