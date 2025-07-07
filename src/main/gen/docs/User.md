

# User


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**id** | **UUID** |  |  [optional] |
|**name** | **String** |  |  [optional] |
|**surname** | **String** |  |  [optional] |
|**patronymic** | **String** |  |  [optional] |
|**phoneNumber** | **String** |  |  [optional] |
|**email** | **String** |  |  [optional] |
|**hashedPassword** | **String** |  |  [optional] |
|**isAdmin** | **Boolean** |  |  [optional] |
|**dateOfBirth** | **LocalDate** |  |  [optional] |
|**age** | **Integer** |  |  [optional] |
|**sex** | [**SexEnum**](#SexEnum) |  |  [optional] |
|**weight** | **Float** |  |  [optional] |
|**height** | **Float** |  |  [optional] |
|**createdAt** | **OffsetDateTime** |  |  [optional] |
|**bio** | **String** |  |  [optional] |
|**avatarUrl** | **String** |  |  [optional] |



## Enum: SexEnum

| Name | Value |
|---- | -----|
| MALE | &quot;male&quot; |
| FEMALE | &quot;female&quot; |
| OTHER | &quot;other&quot; |



