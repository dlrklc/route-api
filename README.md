# 🗺️ **Route Finder** 

## 🔍 **About**
This REST API allows users to see all available routes from source location to destination location. For example, for a user who wants to travel from London city center to Besiktas, a route calculation is made and all routes are listed with transportation types.

Users can find their preferred route with intermediate stops such as **airports**, **bus terminals** to go from the starting point to the destination.
Users can also see available routes for chosen date.

## 🛠️ **Technologies**
- Spring Boot
- Hibernate
- PostgreSQL

## 📝 **Architecture**
1. **Backend (Spring Boot):** The API is developed using Spring Boot. Users' route requests are received through RESTful APIs.
2. **Database (PostgreSQL):** Locations and transporations are stored in the PostgreSQL database.
3. **Frontend (React):** You can access the frontend with this link. [Route UI](https://github.com/dlrklc/route-ui)

## 🚦 **Example Route**
For example, if there are two routes from London City Center to Besiktas;
# Route 1
1. **London City Center → Heathrow Airport (🚌 by Bus, 🚖 by Uber)**
2. **Heathrow Airport → Istanbul Airport (🛫 by Flight)**
3. **Istanbul Airport → Besiktas (🚌 by Bus, 🚖 by Uber)**
# Route 2
1. **London City Center → London City Airport (🚌 by Bus, 🚖 by Uber)**
2. **London City Airport → Istanbul Airport (🛫 by Flight)**
3. **Istanbul Airport → Besiktas (🚌 by Bus, 🚖 by Uber)**
