/** all aspcfs updated with everything before this line 6/17/02
*/

CREATE TABLE survey_answer (
  id SERIAL primary key,
  question_id int not null,
  comments VARCHAR(100) default null,
  quant_ans int DEFAULT -1,
  text_ans VARCHAR(100) DEFAULT null,
  enteredby int not null
);

alter table survey_item add column average float default 0.00;
alter table survey_item add column total1 int default 0;
alter table survey_item add column total2 int default 0;
alter table survey_item add column total3 int default 0;
alter table survey_item add column total4 int default 0;
alter table survey_item add column total5 int default 0;
alter table survey_item add column total6 int default 0;
alter table survey_item add column total7 int default 0;
  
/* June 20 */

INSERT INTO sync_table (system_id, element_name, mapped_class_name, order_id)
 VALUES (2, 'account', 'com.darkhorseventures.cfsbase.Organization', 5);
 
alter table survey_answer add column survey_id int not null default -1;
 
