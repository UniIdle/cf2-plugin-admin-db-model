/* Скрипт для отзыва доступа у "Редактора шаблонов" к определенному шаблону
*/

DELETE FROM %3$s WHERE user_name = '%1$s' AND accessible_class_id = %2$s;
