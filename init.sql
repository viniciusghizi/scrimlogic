CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

ALTER TABLE match_metrics SET (
  autovacuum_vacuum_scale_factor = 0.05,
  autovacuum_analyze_scale_factor = 0.02
);
