drop table if exists backfill_wells;
create table backfill_wells (
    id SERIAL PRIMARY KEY,
    active boolean,
    object_type VARCHAR(255),
    well integer,
    stage integer,
    resource_for_eng_geodetic_survey integer,
    resource_for_eng_geological_survey integer,
    resource_for_lab_research integer,
    resource_for_eng_survey_report integer,
    resource_for_work_doc integer,
    resource_for_proj_doc integer,
    resource_for_est_doc integer
);

drop table if exists oil_well_pads;
create table oil_well_pads (
    id SERIAL PRIMARY KEY,
    active boolean,
    object_type VARCHAR(255),
    well integer,
    stage integer,
    resource_for_eng_geodetic_survey integer,
    resource_for_eng_geological_survey integer,
    resource_for_lab_research integer,
    resource_for_eng_survey_report integer,
    resource_for_work_doc integer,
    resource_for_proj_doc integer,
    resource_for_est_doc integer
);

drop table if exists roads;
create table roads (
    id SERIAL PRIMARY KEY,
    active boolean,
    object_type VARCHAR(255),
    bridge_exist boolean,
    bridge_road_count integer,
    bridge_road_length integer,
    category integer,
    length double precision,
    count integer,
    stage integer,
    resource_for_eng_geodetic_survey integer,
    resource_for_eng_geological_survey integer,
    resource_for_lab_research integer,
    resource_for_eng_survey_report integer,
    resource_for_work_doc integer,
    resource_for_proj_doc integer,
    resource_for_est_doc integer
);

drop table if exists lines;
create table lines (
    id SERIAL PRIMARY KEY,
    active boolean,
    object_type VARCHAR(255),
    power VARCHAR(255),
    length double precision,
--     complexity_of_geology VARCHAR(255),
    stage integer,
    resource_for_eng_geodetic_survey integer,
    resource_for_eng_geological_survey integer,
    resource_for_lab_research integer,
    resource_for_eng_survey_report integer,
    resource_for_work_doc integer,
    resource_for_proj_doc integer,
    resource_for_est_doc integer
);

drop table if exists backfill_sites;
create table backfill_sites (
    id SERIAL PRIMARY KEY,
    active boolean,
    object_type VARCHAR(255),
    square double precision,
    stage integer,
    resource_for_eng_geodetic_survey integer,
    resource_for_eng_geological_survey integer,
    resource_for_lab_research integer,
    resource_for_eng_survey_report integer,
    resource_for_work_doc integer,
    resource_for_proj_doc integer,
    resource_for_est_doc integer
);

drop table if exists vvps;
create table vvps (
    id SERIAL PRIMARY KEY,
    active boolean,
    object_type VARCHAR(255),
    helicopter_model VARCHAR(255),
    lighting_equipment boolean,
    hall_exist boolean,
    square double precision,
    count integer,
    stage integer,
    resource_for_eng_geodetic_survey integer,
    resource_for_eng_geological_survey integer,
    resource_for_lab_research integer,
    resource_for_eng_survey_report integer,
    resource_for_work_doc integer,
    resource_for_proj_doc integer,
    resource_for_est_doc integer
);

drop table if exists cable_rack;
create table cable_rack (
    id SERIAL PRIMARY KEY,
    active boolean,
    object_type VARCHAR(255),
    length integer,
--     complexity_of_geology VARCHAR(255),
    stage integer,
    resource_for_eng_geodetic_survey integer,
    resource_for_eng_geological_survey integer,
    resource_for_lab_research integer,
    resource_for_eng_survey_report integer,
    resource_for_work_doc integer,
    resource_for_proj_doc integer,
    resource_for_est_doc integer
);

drop table if exists pipelines;
create table pipelines (
    id SERIAL PRIMARY KEY,
    active boolean,
    object_type VARCHAR(255),
    pipeline_laying_method VARCHAR(255),
    length double precision,
    unit_exist boolean,
    units_valve integer,
    units_SOD integer,
    stage integer,
    resource_for_eng_geodetic_survey integer,
    resource_for_eng_geological_survey integer,
    resource_for_lab_research integer,
    resource_for_eng_survey_report integer,
    resource_for_work_doc integer,
    resource_for_proj_doc integer,
    resource_for_est_doc integer
);

drop table if exists ktplps;
create table ktplps (
    id SERIAL PRIMARY KEY,
    active boolean,
    object_type VARCHAR(255),
    ktplp_type VARCHAR(255),
    count integer,
    stage integer,
    resource_for_eng_geodetic_survey integer,
    resource_for_eng_geological_survey integer,
    resource_for_lab_research integer,
    resource_for_eng_survey_report integer,
    resource_for_work_doc integer,
    resource_for_proj_doc integer,
    resource_for_est_doc integer
);

drop table if exists sikns;
create table sikns (
    id SERIAL PRIMARY KEY,
    active boolean,
    object_type VARCHAR(255),
    sikn_type VARCHAR(255),
    capacity double precision,
    stage integer,
    resource_for_eng_geodetic_survey integer,
    resource_for_eng_geological_survey integer,
    resource_for_lab_research integer,
    resource_for_eng_survey_report integer,
    resource_for_work_doc integer,
    resource_for_proj_doc integer,
    resource_for_est_doc integer
);