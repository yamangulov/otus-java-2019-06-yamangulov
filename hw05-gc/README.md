Сравнение сборщиков мусора:

Проведено сравнение сборщиков мусора на бенчмарке
 в классе Benchmark. Сравнивались три вида GC: G1GC, Serial GC и Parallel GC
 
 Данные не только выводились в консоль и собирались в логи трех разных сборщиков, но и автоматически суммировались сразу в программе, чтобы не нужно было вручную или отдельной программой парсить логи. Автоматически собирались кол-во сборок young generation и old generation для каждого профиля запуска из трех:
 
 
  * -Xms512m
  * -Xmx512m
  *
  * -Xlog:gc=debug:file=./hw05-gc/logs/gc-%p-%t.log:tags,uptime,time,level:filecount=5,filesize=20m
  *
  * Garbage Collectors:
  * -XX:+UseSerialGC
  * -XX:+UseParallelGC
  * -XX:+UseG1GC
  
  Данные после суммирования сохранялись в файле ./hw05-gc/logs/report.log раздельно для каждого сборщика и для каждого поколения в сборщике.
  На всех трех профилях "время жизни" бенчмарка оказалось примерно одинаковым - чуть меньше 6 минут, ощутимой разницы не было. Однако, у сборщика G1GC оказалось существенно меньше время, затраченное на сборки в области старого поколения. В области молодого поколения время на сборки мало отличается от осталльных сборщиков. Почему так - G1GC разбивает области на большое кол-во участков, к каждой из которых можно обращаться независимо от остальных, параллельно. Это увеличивает интенсивность сборок для молодого поколения и в старое поколение чрезмерное кол-во объектов не переходит. Уменьшается необходимость интенсивно загружать сборщик в областях старого поколения. И в целом мы видим заметный выигрыш у сборщика G1GC.
  Но! Особо хочу отметить, что полученные данные справедливы только для данного бенчмарка. В нем создается ArrayList и затем организуется подтекание по памяти. Очень вероятно, что результаты будут зависеть от типа приложения, которое будет использоваться в качестве бенчмарка. Думаю, что данное ДЗ полезно как практика для построения собственного логирования работы GC и по аналогии можно подготовить логирование GC для любого боевого проекта в целом или в отдельных частях кода. В боевых условиях выводы могут быть другими, в результате чего можно подобрать наиболее оптимальные параметры для запуска VM, они могут зависеть от приложения.
  