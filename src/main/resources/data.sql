INSERT INTO backfill_wells (id, active, object_type, well, stage, resource_for_eng_survey, resource_for_lab_research, resource_for_eng_survey_report,
                            resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (1, 'true', 'AREA', 6, 1, 10, 10, 10, 10, 21, 21);
INSERT INTO backfill_wells (id, active, object_type, well, stage, resource_for_eng_survey, resource_for_lab_research, resource_for_eng_survey_report,
                            resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (2, 'true', 'AREA', 12, 1, 12, 12, 10, 13, 21, 21);
INSERT INTO backfill_wells (id, active, object_type, well, stage, resource_for_eng_survey, resource_for_lab_research, resource_for_eng_survey_report,
                            resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (3, 'true', 'AREA', 18, 1, 14, 14, 10, 17, 21, 21);
INSERT INTO backfill_wells (id, active, object_type, well, stage, resource_for_eng_survey, resource_for_lab_research, resource_for_eng_survey_report,
                            resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (4, 'true', 'AREA', 24, 1, 16, 16, 10, 20, 21, 21);

INSERT INTO roads (id, active, object_type, bridge_exist, bridge_road_count, bridge_road_length, category, length, stage, resource_for_eng_survey,
                   resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (1, 'true', 'LINEAR', 'false', 0, 0, 4, 1, 1, 10, 10, 10, 10, 21, 21);
INSERT INTO roads (id, active, object_type, bridge_exist, bridge_road_count, bridge_road_length, category, length, stage, resource_for_eng_survey,
                   resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (2, 'true', 'LINEAR', 'false', 0, 0, 4, 5, 1, 12, 12, 10, 12, 21, 21);
INSERT INTO roads (id, active, object_type, bridge_exist, bridge_road_count, bridge_road_length, category, length, stage, resource_for_eng_survey,
                   resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (3, 'true', 'LINEAR', 'false', 0, 0, 4, 10, 1, 14, 14, 12, 14, 21, 21);
INSERT INTO roads (id, active, object_type, bridge_exist, bridge_road_count, bridge_road_length, category, length, stage, resource_for_eng_survey,
                   resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (4, 'true', 'LINEAR', 'false', 0, 0, 4, 15, 1, 16, 16, 12, 16, 21, 21);
INSERT INTO roads (id, active, object_type, bridge_exist, bridge_road_count, bridge_road_length, category, length, stage, resource_for_eng_survey,
                   resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (5, 'true', 'LINEAR', 'false', 0, 0, 4, 20, 1, 18, 18, 12, 18, 21, 21);
INSERT INTO roads (id, active, object_type, bridge_exist, bridge_road_count, bridge_road_length, category, length, stage, resource_for_eng_survey,
                   resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (6, 'true', 'LINEAR', 'false', 0, 0, 3, 1, 1, 10, 10, 10, 12, 21, 21);
INSERT INTO roads (id, active, object_type, bridge_exist, bridge_road_count, bridge_road_length, category, length, stage, resource_for_eng_survey,
                   resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (7, 'true', 'LINEAR', 'false', 0, 0, 3, 5, 1, 12, 12, 10, 14, 21, 21);
INSERT INTO roads (id, active, object_type, bridge_exist, bridge_road_count, bridge_road_length, category, length, stage, resource_for_eng_survey,
                   resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (8, 'true', 'LINEAR', 'false', 0, 0, 3, 10, 1, 14, 14, 12, 17, 21, 21);
INSERT INTO roads (id, active, object_type, bridge_exist, bridge_road_count, bridge_road_length, category, length, stage, resource_for_eng_survey,
                   resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (9, 'true', 'LINEAR', 'false', 0, 0, 3, 15, 1, 16, 16, 12, 20, 21, 21);
INSERT INTO roads (id, active, object_type, bridge_exist, bridge_road_count, bridge_road_length, category, length, stage, resource_for_eng_survey,
                   resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (10, 'true', 'LINEAR', 'false', 0, 0, 3, 20, 1, 18, 18, 12, 23, 21, 21);

INSERT INTO lines (id, active, object_type, power, length, complexity_of_geology, stage, resource_for_eng_survey,
                   resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (1, 'true', 'LINEAR', 10, 1, 'EASY', 1, 10, 10, 10, 10, 21, 21);
INSERT INTO lines (id, active, object_type, power, length, complexity_of_geology, stage, resource_for_eng_survey,
                   resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (2, 'true', 'LINEAR', 10, 5, 'EASY', 1, 12, 12, 10, 12, 21, 21);
INSERT INTO lines (id, active, object_type, power, length, complexity_of_geology, stage, resource_for_eng_survey,
                   resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (3, 'true', 'LINEAR', 10, 10, 'EASY', 1, 14, 14, 10, 16, 21, 21);
INSERT INTO lines (id, active, object_type, power, length, complexity_of_geology, stage, resource_for_eng_survey,
                   resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (4, 'true', 'LINEAR', 10, 15, 'EASY', 1, 16, 16, 12, 20, 21, 21);
INSERT INTO lines (id, active, object_type, power, length, complexity_of_geology, stage, resource_for_eng_survey,
                   resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (5, 'true', 'LINEAR', 10, 20, 'EASY', 1, 18, 18, 12, 24, 21, 21);
INSERT INTO lines (id, active, object_type, power, length, complexity_of_geology, stage, resource_for_eng_survey,
                   resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (6, 'true', 'LINEAR', 35, 1, 'EASY', 1, 10, 5, 8, 10, 21, 21);
INSERT INTO lines (id, active, object_type, power, length, complexity_of_geology, stage, resource_for_eng_survey,
                   resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (7, 'true', 'LINEAR', 35, 5, 'EASY', 1, 10, 7, 8, 12, 21, 21);
INSERT INTO lines (id, active, object_type, power, length, complexity_of_geology, stage, resource_for_eng_survey,
                   resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (8, 'true', 'LINEAR', 35, 10, 'EASY', 1, 12, 9, 10, 16, 21, 21);
INSERT INTO lines (id, active, object_type, power, length, complexity_of_geology, stage, resource_for_eng_survey,
                   resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (9, 'true', 'LINEAR', 35, 15, 'EASY', 1, 14, 11, 12, 20, 21, 21);
INSERT INTO lines (id, active, object_type, power, length, complexity_of_geology, stage, resource_for_eng_survey,
                   resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (10, 'true', 'LINEAR', 35, 20, 'EASY', 1, 16, 13, 12, 24, 21, 21);
INSERT INTO lines (id, active, object_type, power, length, complexity_of_geology, stage, resource_for_eng_survey,
                   resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (11, 'true', 'LINEAR', 110, 1, 'EASY', 1, 10, 5, 8, 20, 21, 21);
INSERT INTO lines (id, active, object_type, power, length, complexity_of_geology, stage, resource_for_eng_survey,
                   resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (12, 'true', 'LINEAR', 110, 5, 'EASY', 1, 10, 7, 8, 22, 21, 21);
INSERT INTO lines (id, active, object_type, power, length, complexity_of_geology, stage, resource_for_eng_survey,
                   resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (13, 'true', 'LINEAR', 110, 10, 'EASY', 1, 12, 9, 10, 26, 21, 21);
INSERT INTO lines (id, active, object_type, power, length, complexity_of_geology, stage, resource_for_eng_survey,
                   resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (14, 'true', 'LINEAR', 110, 15, 'EASY', 1, 14, 11, 12, 30, 21, 21);
INSERT INTO lines (id, active, object_type, power, length, complexity_of_geology, stage, resource_for_eng_survey,
                   resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (15, 'true', 'LINEAR', 110, 20, 'EASY', 1, 16, 13, 12, 34, 21, 21);

INSERT INTO backfill_sites (id, active, object_type, square, stage, resource_for_eng_survey, resource_for_lab_research, resource_for_eng_survey_report,
                            resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (1, 'true', 'AREA', 1, 1, 10, 5, 8, 5, 21, 21);
INSERT INTO backfill_sites (id, active, object_type, square, stage, resource_for_eng_survey, resource_for_lab_research, resource_for_eng_survey_report,
                            resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (2, 'true', 'AREA', 2, 1, 10, 7, 8, 7, 21, 21);
INSERT INTO backfill_sites (id, active, object_type, square, stage, resource_for_eng_survey, resource_for_lab_research, resource_for_eng_survey_report,
                            resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (3, 'true', 'AREA', 3, 1, 12, 9, 9, 9, 21, 21);
INSERT INTO backfill_sites (id, active, object_type, square, stage, resource_for_eng_survey, resource_for_lab_research, resource_for_eng_survey_report,
                            resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (4, 'true', 'AREA', 4, 1, 14, 11, 9, 10, 21, 21);
INSERT INTO backfill_sites (id, active, object_type, square, stage, resource_for_eng_survey, resource_for_lab_research, resource_for_eng_survey_report,
                            resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (5, 'true', 'AREA', 5, 1, 16, 13, 10, 11, 21, 21);
INSERT INTO backfill_sites (id, active, object_type, square, stage, resource_for_eng_survey, resource_for_lab_research, resource_for_eng_survey_report,
                            resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (6, 'true', 'AREA', 6, 1, 18, 15, 10, 12, 21, 21);

-- INSERT INTO vecs (id, active, object_type, power, stock_exist, square, stage, resource)
-- VALUES (1, 'true', 'AREA', 0, 'false', 1, 1, 5);
-- INSERT INTO vecs (id, active, object_type, power, stock_exist, square, stage, resource)
-- VALUES (2, 'true', 'AREA', 0, 'false', 2, 1, 7);
-- INSERT INTO vecs (id, active, object_type, power, stock_exist, square, stage, resource)
-- VALUES (3, 'true', 'AREA', 0, 'false', 3, 1, 9);
-- INSERT INTO vecs (id, active, object_type, power, stock_exist, square, stage, resource)
-- VALUES (4, 'true', 'AREA', 0, 'false', 4, 1, 11);

INSERT INTO vvps (id, active, object_type, helicopter_model, lighting_equipment, hall_exist, square, stage, resource_for_eng_survey,
                  resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (1, 'true', 'AREA', 'МИ-8', 'false', 'false', 1, 1, 10, 5, 8, 5, 21, 21);
INSERT INTO vvps (id, active, object_type, helicopter_model, lighting_equipment, hall_exist, square, stage, resource_for_eng_survey,
                  resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (2, 'true', 'AREA', 'МИ-8', 'false', 'false', 2, 1, 10, 7, 8, 7, 21, 21);
INSERT INTO vvps (id, active, object_type, helicopter_model, lighting_equipment, hall_exist, square, stage, resource_for_eng_survey,
                  resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (3, 'true', 'AREA', 'МИ-16', 'false', 'false', 3, 1, 12, 9, 9, 9, 21, 21);
INSERT INTO vvps (id, active, object_type, helicopter_model, lighting_equipment, hall_exist, square, stage, resource_for_eng_survey,
                  resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (4, 'true', 'AREA', 'МИ-16', 'false', 'false', 4, 1, 14, 11, 9, 11, 21, 21);

INSERT INTO cable_rack (id, active, object_type, length, complexity_of_geology, stage, resource_for_eng_survey,
                        resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (1, 'true', 'LINEAR', 100, 'EASY', 1, 10, 5, 8, 8, 21, 21);
INSERT INTO cable_rack (id, active, object_type, length, complexity_of_geology, stage, resource_for_eng_survey,
                        resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (2, 'true', 'LINEAR', 500, 'EASY', 1, 10, 7, 8, 12, 21, 21);
INSERT INTO cable_rack (id, active, object_type, length, complexity_of_geology, stage, resource_for_eng_survey,
                        resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (3, 'true', 'LINEAR', 1000, 'EASY', 1, 12, 9, 10, 16, 21, 21);
INSERT INTO cable_rack (id, active, object_type, length, complexity_of_geology, stage, resource_for_eng_survey,
                        resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (4, 'true', 'LINEAR', 1500, 'EASY', 1, 14, 11, 12, 20, 21, 21);
INSERT INTO cable_rack (id, active, object_type, length, complexity_of_geology, stage, resource_for_eng_survey,
                        resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (5, 'true', 'LINEAR', 2000, 'EASY', 1, 16, 13, 12, 24, 21, 21);

-- INSERT INTO vjks (id, active, object_type, capacity, square, stage, resource)
-- VALUES (1, 'true', 'AREA', 50, 1, 1, 5);
-- INSERT INTO vjks (id, active, object_type, capacity, square, stage, resource)
-- VALUES (2, 'true', 'AREA', 100, 2, 1, 7);
-- INSERT INTO vjks (id, active, object_type, capacity, square, stage, resource)
-- VALUES (3, 'true', 'AREA', 150, 3, 1, 9);
-- INSERT INTO vjks (id, active, object_type, capacity, square, stage, resource)
-- VALUES (4, 'true', 'AREA', 200, 4, 1, 11);

INSERT INTO pipelines (id, active, object_type, pipeline_laying_method, length, complexity_of_geology, units_valve, units_SOD, stage, resource_for_eng_survey,
                       resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (1, 'true', 'LINEAR', 'UNDERGROUND', 1, 'EASY', 0, 0, 1, 10, 5, 8, 10, 21, 21);
INSERT INTO pipelines (id, active, object_type, pipeline_laying_method, length, complexity_of_geology, units_valve, units_SOD, stage, resource_for_eng_survey,
                       resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (2, 'true', 'LINEAR', 'UNDERGROUND', 1, 'EASY', 0, 2, 1, 11, 6, 8, 20, 21, 21);
INSERT INTO pipelines (id, active, object_type, pipeline_laying_method, length, complexity_of_geology, units_valve, units_SOD, stage, resource_for_eng_survey,
                       resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (3, 'true', 'LINEAR', 'UNDERGROUND', 1, 'DIFFICULT', 0, 0, 1, 10, 5, 8, 12, 21, 21);
INSERT INTO pipelines (id, active, object_type, pipeline_laying_method, length, complexity_of_geology, units_valve, units_SOD, stage, resource_for_eng_survey,
                       resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (4, 'true', 'LINEAR', 'UNDERGROUND', 1, 'DIFFICULT', 0, 2, 1, 11, 6, 8, 24, 21, 21);
INSERT INTO pipelines (id, active, object_type, pipeline_laying_method, length, complexity_of_geology, units_valve, units_SOD, stage, resource_for_eng_survey,
                       resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (5, 'true', 'LINEAR', 'ABOVEGROUND', 1, 'EASY', 0, 0, 1, 12, 9, 10, 20, 21, 21);
INSERT INTO pipelines (id, active, object_type, pipeline_laying_method, length, complexity_of_geology, units_valve, units_SOD, stage, resource_for_eng_survey,
                       resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (6, 'true', 'LINEAR', 'ABOVEGROUND', 1, 'EASY', 0, 2, 1, 12, 9, 10, 30, 21, 21);
INSERT INTO pipelines (id, active, object_type, pipeline_laying_method, length, complexity_of_geology, units_valve, units_SOD, stage, resource_for_eng_survey,
                       resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (7, 'true', 'LINEAR', 'ABOVEGROUND', 1, 'DIFFICULT', 0, 0, 1, 12, 9, 10, 24, 21, 21);
INSERT INTO pipelines (id, active, object_type, pipeline_laying_method, length, complexity_of_geology, units_valve, units_SOD, stage, resource_for_eng_survey,
                       resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (8, 'true', 'LINEAR', 'ABOVEGROUND', 1, 'DIFFICULT', 0, 2, 1, 12, 9, 10, 36, 21, 21);
INSERT INTO pipelines (id, active, object_type, pipeline_laying_method, length, complexity_of_geology, units_valve, units_SOD, stage, resource_for_eng_survey,
                       resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (9, 'true', 'LINEAR', 'UNDERGROUND', 5, 'EASY', 2, 2, 1, 14, 12, 12, 40, 21, 21);
INSERT INTO pipelines (id, active, object_type, pipeline_laying_method, length, complexity_of_geology, units_valve, units_SOD, stage, resource_for_eng_survey,
                       resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (10, 'true', 'LINEAR', 'UNDERGROUND', 20, 'EASY', 10, 10, 1, 30, 25, 20, 60, 21, 21);
INSERT INTO pipelines (id, active, object_type, pipeline_laying_method, length, complexity_of_geology, units_valve, units_SOD, stage, resource_for_eng_survey,
                       resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (11, 'true', 'LINEAR', 'UNDERGROUND', 20, 'MEDIUM', 10, 10, 1, 30, 25, 20, 62, 21, 21);
INSERT INTO pipelines (id, active, object_type, pipeline_laying_method, length, complexity_of_geology, units_valve, units_SOD, stage, resource_for_eng_survey,
                       resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (12, 'true', 'LINEAR', 'UNDERGROUND', 20, 'DIFFICULT', 10, 10, 1, 30, 25, 20, 64, 21, 21);
INSERT INTO pipelines (id, active, object_type, pipeline_laying_method, length, complexity_of_geology, units_valve, units_SOD, stage, resource_for_eng_survey,
                       resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (13, 'true', 'LINEAR', 'ABOVEGROUND', 5, 'EASY', 2, 2, 1, 28, 25, 14, 50, 21, 21);
INSERT INTO pipelines (id, active, object_type, pipeline_laying_method, length, complexity_of_geology, units_valve, units_SOD, stage, resource_for_eng_survey,
                       resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (14, 'true', 'LINEAR', 'ABOVEGROUND', 20, 'EASY', 10, 10, 1, 40, 35, 22, 70, 21, 21);
INSERT INTO pipelines (id, active, object_type, pipeline_laying_method, length, complexity_of_geology, units_valve, units_SOD, stage, resource_for_eng_survey,
                       resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (15, 'true', 'LINEAR', 'ABOVEGROUND', 20, 'MEDIUM', 10, 10, 1, 40, 35, 22, 74, 21, 21);
INSERT INTO pipelines (id, active, object_type, pipeline_laying_method, length, complexity_of_geology, units_valve, units_SOD, stage, resource_for_eng_survey,
                       resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (16, 'true', 'LINEAR', 'ABOVEGROUND', 20, 'DIFFICULT', 10, 10, 1, 40, 35, 22, 78, 21, 21);

INSERT INTO pipelines (id, active, object_type, pipeline_laying_method, length, complexity_of_geology, units_valve, units_SOD, stage, resource_for_eng_survey,
                       resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (17, 'true', 'LINEAR', 'GROUND', 20, 'EASY', 10, 10, 1, 21, 32, 32, 60, 21, 21);
INSERT INTO pipelines (id, active, object_type, pipeline_laying_method, length, complexity_of_geology, units_valve, units_SOD, stage, resource_for_eng_survey,
                       resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (18, 'true', 'LINEAR', 'GROUND', 20, 'MEDIUM', 10, 10, 1, 21, 32, 32, 62, 21, 21);
INSERT INTO pipelines (id, active, object_type, pipeline_laying_method, length, complexity_of_geology, units_valve, units_SOD, stage, resource_for_eng_survey,
                       resource_for_lab_research, resource_for_eng_survey_report, resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (19, 'true', 'LINEAR', 'GROUND', 20, 'DIFFICULT', 10, 10, 1, 21, 32, 32, 64, 21, 21);

INSERT INTO ktplps (id, active, object_type, ktplp_type, count, stage, resource_for_eng_survey, resource_for_lab_research, resource_for_eng_survey_report,
                            resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (1, 'true', 'AREA', 'KTPLP6_04', 1, 1, 3, 1, 3, 10, 15, 10);
INSERT INTO ktplps (id, active, object_type, ktplp_type, count, stage, resource_for_eng_survey, resource_for_lab_research, resource_for_eng_survey_report,
                    resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (2, 'true', 'AREA', 'KTPLP10_04', 1, 1, 3, 1, 3, 11, 15, 10);
INSERT INTO ktplps (id, active, object_type, ktplp_type, count, stage, resource_for_eng_survey, resource_for_lab_research, resource_for_eng_survey_report,
                    resource_for_work_doc, resource_for_proj_doc, resource_for_est_doc)
VALUES (3, 'true', 'AREA', 'KTPLP35_04', 1, 1, 3, 1, 3, 12, 15, 10);

