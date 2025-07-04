# UserMmrApi

All URIs are relative to *https://api.user.local*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**usersIdMmrGet**](UserMmrApi.md#usersIdMmrGet) | **GET** /users/{id}/mmr | Получить MMR пользователя по видам спорта |


<a id="usersIdMmrGet"></a>
# **usersIdMmrGet**
> List&lt;UserSportMmr&gt; usersIdMmrGet(id)

Получить MMR пользователя по видам спорта

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.UserMmrApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://api.user.local");

    UserMmrApi apiInstance = new UserMmrApi(defaultClient);
    UUID id = UUID.randomUUID(); // UUID | Идентификатор пользователя
    try {
      List<UserSportMmr> result = apiInstance.usersIdMmrGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UserMmrApi#usersIdMmrGet");
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

[**List&lt;UserSportMmr&gt;**](UserSportMmr.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | MMR по видам спорта |  -  |

