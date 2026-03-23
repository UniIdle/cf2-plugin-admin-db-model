/* Скрипт для разблокировки группы свойств */

DELETE FROM %2$s WHERE unaccessible_group = '%1$s';
