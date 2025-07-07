

# UserCreateRequest


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**name** | **String** |  |  |
|**surname** | **String** |  |  |
|**patronymic** | **String** |  |  [optional] |
|**phoneNumber** | **String** |  |  |
|**email** | **String** |  |  [optional] |
|**hashedPassword** | **String** |  |  |
|**isAdmin** | **Boolean** |  |  [optional] |
|**dateOfBirth** | **LocalDate** |  |  [optional] |
|**age** | **Integer** |  |  [optional] |
|**sex** | [**SexEnum**](#SexEnum) |  |  [optional] |
|**weight** | **Float** |  |  [optional] |
|**height** | **Float** |  |  [optional] |
|**bio** | **String** |  |  [optional] |
|**avatarUrl** | **String** |  |  [optional] |



## Enum: SexEnum

| Name | Value |
|---- | -----|
| MALE | &quot;male&quot; |
| FEMALE | &quot;female&quot; |
| OTHER | &quot;other&quot; |



