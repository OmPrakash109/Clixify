package com.project.clixify_backend_sb.dtos;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ClickEventDTO      //DTO class for ClickEvent - we use ClickEventDTO instead of ClickEvent Entity as we don't want to expose all the fields of the Entity to the client.
{
    private LocalDate clickDate;    //This field is used to store the date of the click event.
                                    //AND RETURN TYPE MUST BE 'LocalDate' as 'entry.getKey()' returns a LocalDate in 'getClickEventByDate' method of UrlMappingService.
    private Long count; //To store Number of clicks on the short URL
}

/*
we see the same name in output as DTO fields like here we have 'clickDate' and 'count' as fields in DTO.
Example:

[
    {
        "clickData": "2024-12-01",
        "count": 10
    },
    {
        "clickDate": "2024-12-02",
        "count": 20
    }
]

if we would have used 'cD' instead of 'clickDate' then the output would have been:

[
    {
        "cD": "2024-12-01",
        "count": 10
    },
    {
        "cD": "2024-12-02",
        "count": 20
    }
]

And each DTO object contains 2 fields: 'clickDate' and 'count'.
and in UrlMappingController/getUrlAnalytics method, we return a List of ClickEventDTO objects as response, just like shown in the example above.

*/
