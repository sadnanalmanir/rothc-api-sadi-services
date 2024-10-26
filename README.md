# rothc-api-sadi-services
SADI Services exposing RESTful API [endpoints](https://api-nwfp.rothamsted.ac.uk/api/utils/v1/fetchRothcdata/) of the [North Wyke Farm Platform](https://www.rothamsted.ac.uk/national-capability/north-wyke-farm-platform) Data 

## SADI services
The following table shows the SADI services, their CRUD operations, and the endpoints they expose

| SADI Service                      | Description                                                                                | HTTP Method | API Endpoint                                           |
|-----------------------------------|--------------------------------------------------------------------------------------------|-------------|--------------------------------------------------------|
| fetchRothcInputClayParams         | Fetch catchment clay value based on the start date, end date, catchment id and field id    | POST        | https://api-nwfp.rothamsted.ac.uk/api/utils/v1/fetchRothcdata/     |
| fetchRothcInputDepthParams        | Fetch catchment depth value based on the start date, end date, catchment id and field id   | POST        | https://api-nwfp.rothamsted.ac.uk/api/utils/v1/fetchRothcdata/     |
| fetchRothcInputIomParams          | Fetch catchment iom value based on the start date, end date, catchment id and field id     | POST        | https://api-nwfp.rothamsted.ac.uk/api/utils/v1/fetchRothcdata/     |
| fetchRothcInputNstepsParams       | Fetch catchment nsteps value based on the start date, end date, catchment id and field id  | POST        | https://api-nwfp.rothamsted.ac.uk/api/utils/v1/fetchRothcdata/     |
| fetchRothcMonthlyDataCinpParams   | Fetch catchment C_inp value based on the start date, end date, catchment id and field id   | POST        | https://api-nwfp.rothamsted.ac.uk/api/utils/v1/fetchRothcdata/     |
| fetchRothcMonthlyDataDpmRpmParams | Fetch catchment DPM_PRM value based on the start date, end date, catchment id and field id | POST        | https://api-nwfp.rothamsted.ac.uk/api/utils/v1/fetchRothcdata/     |
| fetchRothcMonthlyDataEvapParams   | Fetch catchment Evap value based on the start date, end date, catchment id and field id    | POST        | https://api-nwfp.rothamsted.ac.uk/api/utils/v1/fetchRothcdata/     |
| fetchRothcMonthlyDataFymParams    | Fetch catchment FYM value based on the start date, end date, catchment id and field id     | POST        | https://api-nwfp.rothamsted.ac.uk/api/utils/v1/fetchRothcdata/     |
| fetchRothcMonthlyDataPcParams     | Fetch catchment PC value based on the start date, end date, catchment id and field id      | POST        | https://api-nwfp.rothamsted.ac.uk/api/utils/v1/fetchRothcdata/     |
| fetchRothcMonthlyDataRainParams   | Fetch catchment Rain value based on the start date, end date, catchment id and field id    | POST        | https://api-nwfp.rothamsted.ac.uk/api/utils/v1/fetchRothcdata/     |
| fetchRothcMonthlyDataTmpParams    | Fetch catchment Tmp value based on the start date, end date, catchment id and field id     | POST        | https://api-nwfp.rothamsted.ac.uk/api/utils/v1/fetchRothcdata/     |
| fetchRothcMonthlyDataModernParams | Fetch catchment modern value based on the start date, end date, catchment id and field id  | POST        | https://api-nwfp.rothamsted.ac.uk/api/utils/v1/fetchRothcdata/     |
| fetchRothcMonthlyDataMonthParams  | Fetch catchment month value based on the start date, end date, catchment id and field id   | POST        | https://api-nwfp.rothamsted.ac.uk/api/utils/v1/fetchRothcdata/     |
| fetchRothcMonthlyDataYearParams   | Fetch catchment year value based on the start date, end date, catchment id and field id    | POST        | https://api-nwfp.rothamsted.ac.uk/api/utils/v1/fetchRothcdata/     |


## How to deploy
You need to have [docker](https://docs.docker.com/get-docker/) and [Docker Compose](https://docs.docker.com/compose/install/) 
installed in your system. The following command uses `docker-compose.yaml` and `Dockerfile` to compile and package the source, 
deploy the ontologies, and the services on Tomcat server at http://localhost:8080.

### Clone the repository
```shell
$ git clone https://github.com/sadnanalmanir/rothc-api-sadi-services.git 
```

### Move into the repository
```shell
$ cd rothc-api-sadi-services
```

Rename the `.env.example` file in the `src/main/resources` folder into a file named `.env`. For example:

```shell
$ mv src/main/resources/.env.example src/main/resources/.env
```

You will need required credentials in order to run the demo. The following credentials need to be set in the `.env` file. 

```
NWFP_DATA_API_KEY=<replace-with-your-nwfp-data-api-key>
NWFP_DATA_API_SECRET=<replace-with-your-nwfp-data-api-secret>
```

### Use docker compose command

```shell
$ docker compose up --build
```

After the deployment, open the browser to view:

- SADI services at http://localhost:8080/rothc-api-sadi-services
- Domain ontology at http://localhost:8080/ontology/domain-ontology/rothc.owl
- Service ontologies at
  - http://localhost:8080/ontology/service-ontology/fetchRothcInputClayParams.owl
  - http://localhost:8080/ontology/service-ontology/fetchRothcInputDepthParams.owl
  - http://localhost:8080/ontology/service-ontology/fetchRothcInputIomParams.owl
  - http://localhost:8080/ontology/service-ontology/fetchRothcInputNstepsParams.owl
  - http://localhost:8080/ontology/service-ontology/fetchRothcMonthlyDataCinpParams.owl
  - http://localhost:8080/ontology/service-ontology/fetchRothcMonthlyDataDpmRpmParams.owl
  - http://localhost:8080/ontology/service-ontology/fetchRothcMonthlyDataEvapParams.owl
  - http://localhost:8080/ontology/service-ontology/fetchRothcMonthlyDataFymParams.owl
  - http://localhost:8080/ontology/service-ontology/fetchRothcMonthlyDataPcParams.owl
  - http://localhost:8080/ontology/service-ontology/fetchRothcMonthlyDataRainParams.owl
  - http://localhost:8080/ontology/service-ontology/fetchRothcMonthlyDataTmpParams.owl
  - http://localhost:8080/ontology/service-ontology/fetchRothcMonthlyDataModernParams.owl
  - http://localhost:8080/ontology/service-ontology/fetchRothcMonthlyDataMonthParams.owl
  - http://localhost:8080/ontology/service-ontology/fetchRothcMonthlyDataYearParams.owl

## Test the service
The `curl` command can be used to test each service
- Retrieve the service description
```shell
$ curl SERVICE_URL
```
- Test the service with input serialized in RDF
```shell
$ curl -H 'Content-Type: text/rdf' -H 'Accept: text/rdf' --data @PATH_TO_RDF_DATA SERVICE_URL
```
- Test the service with input serialized in n3
```shell
$ curl -H 'Content-Type: text/rdf+n3' -H 'Accept: text/rdf+n3' --data @PATH_TO_N3_DATA SERVICE_URL
```

For instance, use the following commands to test the getCatchmentInfo service
```shell
$ curl http://localhost:8080/rothc-api-sadi-services/fetchRothcInputClayParams
```
```shell
$ curl -H 'Content-Type: text/rdf' -H 'Accept: text/rdf' --data @./src/test/inputdata/fetchRothcInputClayParams/1.rdf http://localhost:8080/rothc-api-sadi-services/fetchRothcInputClayParams
```
```shell
$ curl -H 'Content-Type: text/rdf+n3' -H 'Accept: text/rdf+n3' --data @./src/test/inputdata/fetchRothcInputClayParams/1.n3 http://localhost:8080/rothc-api-sadi-services/fetchRothcInputClayParams
```