===============
Invoice Summary
===============

[User Info]
- Username: ${userData.username}
- Name: ${userData.name}
- Surname: ${userData.surname}
- Tax Code: ${userData.taxCode}
- Address: ${userData.address}
- City: ${userData.city}
- Zip Code: ${userData.zipCode}
- Phone: ${userData.phone}
- Email: ${userData.email}

[Poster Request]
- Request ID: ${checkAvailabilityResponse.requestId}
- Poster Format: ${posterAvailabilityRequest.posterFormat}

[Selected Zones]
<#list selectedZones as format, zones>
  ${format}:
  <#list zones as zone>
    - Zone ID: ${zone.id}, Name: ${zone.name}, Price: ${zone.price}
  </#list>
</#list>

[Invoice]
- Invoice Number: ${orderInformation.invoiceNumber}
- Amount Due: ${orderInformation.amountDue}
