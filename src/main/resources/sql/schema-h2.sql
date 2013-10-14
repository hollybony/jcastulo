DROP TABLE IF EXISTS stream_specs_audio_medias;
DROP TABLE IF EXISTS audio_medias;
DROP TABLE IF EXISTS stream_specs;

CREATE TABLE stream_specs (
       name VARCHAR(32) NOT NULL
     , mountPoint VARCHAR(32) NOT NULL
     , PRIMARY KEY (name)
);

CREATE TABLE audio_medias (
       pathname VARCHAR(255) NOT NULL
     , PRIMARY KEY (pathname)
);

CREATE TABLE stream_specs_audio_medias (
       STREAM_SPECS_name VARCHAR(32) NOT NULL
     , audioMedias_pathname VARCHAR(255) NOT NULL
);
