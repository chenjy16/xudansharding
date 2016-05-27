一、目标

1、希望做到操作单库场景一样操作多库，通过标准配置和业方制定的路由规则，
再程序运行时通过标准的sql就可以自动路由到不同的库中，并合并结果集返回给上层.

2、面向jdbc标准




二、聚合函数的使用

支持 min  max  avg  sum  count

目前聚合后的结果集都是使用LinkedList数据结构，所以使用时候要注意返回的结果集个数不能超过集合所能承载的最大个数


使用avg函数的实现思路,举例:  单库时 select   avg(age) from user

 会将sql改成   select  sum（age）， count（age）  from  user,然后   sum（age）/ count（age）得出平均数





三、支持limit分页查询

目前会把各个分库的分页结果集合并到LinkedList集合中，然后排序，取出符合要求的结果集，目前实现方案 只能支持list集合所能装载的最大个数，使用时要注意。


四、对于关联查询

根据业务需要，对多表操作逻辑拆分，进行多次操作。


五、整体架构图

[TC > 分库组件设计文档 > jdbc.png]


六、事务支持情况:

暂时不支持


七、sql解析引擎说明:

只支持一种解析器fdb parser(FoundationDB SQL Parser)，该解析器来源于Apache Derby parser，fdb parser为FoundationDB的一个子项目,目前采用版本是1.3.0,解析性能上不如druid，但是他支持的语法更倾向于标准sql，并且相关资料比druid多，

所以目前采用它来解析，后续会把durid也支持上去。



八、需要重要测试的功能点

1、结果集合并

2、路由功能

3、sql语法支持

4、sql执行性能


九、目前支持的功能点

1、读写分离

2、监控报警:数据源不可用或者恢复正常时报警(已屏蔽不使用)

3、多个读库时支持的负载均衡算法：轮询，有读库的轮询，随机，有权重的随机,  

支持高可用的随机(屏蔽不使用)，

支持高可用的有权重随机（默认，屏蔽不使用）

4、sql中的表名和数据库的不一致，可以支持重写



十、后面要支持的功能

1、增加druid解析sql支持

2、事务增加同步重试和异步补偿功能（思路来源当当网开源的分库组件）



