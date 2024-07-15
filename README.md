# Rate-Limited Notification Service
We have a Notification system that sends out email notifications of various types (status update, daily news, project invitations, etc). We need to protect recipients from getting too many emails, either due to system errors or due to abuse, so let’s limit the number of emails sent to them by implementing a rate-limited version of NotificationService.

The system must reject requests that are over the limit.

### Some sample notification types and rate limit rules, e.g.:

- Status: not more than 2 per minute for each recipient
- News: not more than 1 per day for each recipient
- Marketing: not more than 3 per hour for each recipient
- Etc. these are just samples, the system might have several rate limit rules!
---
The properties file (config.properties) is used to define the rate limits for different types of notifications. Each type of notification will have two properties: 
period and count.

- Notification Type: This is the type of notification for which you are setting the rate limit. For example, it could be "news".
- Period: It is specified in milliseconds. For instance, if you want to limit "news" notifications to once per day, the period would be 86400000 milliseconds (24 hours). 24x60x60x1000
- Count: This defines the maximum number of notifications that can be sent within the specified period. For example, if you want to allow only one "news" notification per day, the count would be 1.

`update.period=60000`
`update.count=2`
###### Update notifications: max 2 per minute (60000 milliseconds in a minute)

---
Technologies: Java 11, JUnit 5, Gradle 7.5
