# 功能背景
基于业务规则中心和业务流程，需要一套耦合性低，易理解，易扩展的业务流程器来支撑业务执行。
商城系统需要基于不同的业务场景执行不同的业务流程

# O2BPM2.0 对比O2BPM1.0
| 
 | O2BPM - v1.0 | O2BPM - v2.0 |
| --- | --- | --- |
| 单线流程 | 支持 | 支持 |
| 条件表达式 | / | 支持 |
| 流程分叉 | / | 支持 |


# 11月版oms 需求对接o2bpm

1. 需求罗列文档链接：
- [配货单指定物流规则](https://o2ec.yuque.com/lot8pd/review/vgwggg)
- [配货单审核规则](https://o2ec.yuque.com/lot8pd/review/uzmcps)
- [赠品发货策略](https://o2ec.yuque.com/lot8pd/review/tg5nag)
2. 可以考虑使用bpm 做的需求
- 配货单指定物流规则
- 配货单审核规则
3. bpm 做的需求标准是什么？
    1. 技术评审判断是否使用bpm
# 一. 业务流程背景调查

1. 滴滴（[turbo](https://o2ec.yuque.com/lot8pd/review/ovzi1c)）和阿里（[compileflow](https://o2ec.yuque.com/lot8pd/review/kh7w4i/edit?toc_node_uuid=OVrtXGilCwuSYPFL)）的流程引擎都参照了BPMN2.0的规范
   |  |  | Activiti | Camunda | Compileflow | turbo |
   | --- | --- | --- | --- | --- | --- |
   |  | 核心表量 | 28 | 22 | 0 | 5 |
   | 特性 | 中断可重入 | √ | √ | × | √ |
   |  | 支持回滚 | × | √ | × | √ |
   |  | 运行模式 | 独立运行和内嵌 | 独立运行和内嵌 | 内嵌 | 内嵌 |
   | 兼容性 | 流程格式 | BPMN2.0、XPDL、PDL | BPMN2.0、XPDL、PDL | BPMN2.0 | BPMN2.0 |
   |  | 支持脚本 | JUEL、groovy | python、ruby、groovy、JUEL | QlExpress | groovy |
   |  | 支持数据库 | Oracle、SQL Server、MySQL | Oracle、SQL Server、MySQL、postgre | 无 | MySQL |

2. ![image.png](https://cdn.nlark.com/yuque/0/2022/png/22130084/1664185455627-bcf1612f-e4ac-4f85-b83a-3cbf529a1c84.png#averageHue=%23f3f3f2&clientId=u2c87f17e-0444-4&errorMessage=unknown%20error&from=paste&height=71&id=u9a5d389a&originHeight=71&originWidth=211&originalType=binary&ratio=1&rotation=0&showTitle=false&size=6405&status=error&style=none&taskId=u26c741c6-fd86-497a-8a5f-d650ce8816c&title=&width=211)研究
3. 哪些不满足o2的需求？(条件表达式的可视化编辑， 流程模型的存储和解析, 阿里和滴滴时基于xml格式的业务模型，而O2的业务模型是JSON格式，且更易编辑，表达式规则引擎不能兼容多个)
4. 自研的优势？更符合O2的业务场景，可以快速更新迭代

# 二. 国际bpmn2.0节点定义

1. 业务过程模型和符号定义图

[BPMN2_0_Poster_CHN.PDF](https://o2ec.yuque.com/attachments/yuque/0/2022/pdf/29446496/1664173929817-f3bbb6a1-18ae-49e1-bf8c-e354b46303b4.pdf)

2. BPMN2.0元素分类

![](https://cdn.nlark.com/yuque/0/2022/jpeg/29446496/1664173905690-934e07d4-6fda-4c61-a2b9-10be39aa49ca.jpeg)
# 三. O2业务流程整体架构设计
## 3.1. 流程引擎架构图
![](https://cdn.nlark.com/yuque/0/2022/jpeg/22130084/1664210378640-01843f71-2624-4de7-9497-630e4eef10f5.jpeg)

- 服务层：服务层主要是对外提供服务的入口层，提供的服务包括业务流程配置、业务规则配置、服务参数设置等，所有的服务全部都是通过数据接入模块接入数据
- 引擎层：引擎层是整个平台的核心，主要包括了执行规则的规则引擎、执行业务流程流转的流程引擎
    - 流程引擎功能：负责调度节点，负责流程的开始和结束，负责全局异常捕获。
    - 规则引擎功能：负责条件表达式的处理。
    - 流程监控功能：本次不做设计。
- 存储层：存储层包括了任务节点存的存储、任务参数定义存储 的存储、业务模型的存储

## 3.2. 业务流程泳道图
![](https://cdn.nlark.com/yuque/0/2022/jpeg/29446496/1664202827361-f98d62f0-fe3d-47e0-a093-fd6a3087a953.jpeg)
# 四、O2BPM2.0元素介绍

- 开始节点 (StartEvent)：标识流程的开始；
- 结束节点(EndEvent)：标识流程的结束；
- 网关
    - 排他网关(网关必须有条件流，建议有默认流)，与SequenceFlow配合使用，用于描述SequenceFlow的执行策略。例如：

                同一时刻的同一个实例中，根据指定输入，有且只有一条路径(SequenceFlow)被命中；

    - 并行网关（暂不实现）
- 线
    - 顺序流 sequence flow 记录节点之间的执行顺序，
    - 默认流 defalut flow
    - 条件流 condition flow：编辑条件表达式 ; 调用规则引擎。 可以配置执行的条件conditions（比如用户点击了“同意”作为输入），conditions只有在与网关节点Gateway配合使用时生效，由Gateway决定conditions的执行策略。
- 任务
    - 服务任务（系统内部自行执行任务的节点；替换bpm2.0概念，使用bean节点实现）

# 五、O2bpm2.0指定物流 流程图
![diagram.svg](https://cdn.nlark.com/yuque/0/2022/svg/29446496/1664204165979-2864fe94-fe92-41d2-804a-9344e70edc35.svg#clientId=ucf4183f1-9e38-4&errorMessage=unknown%20error&from=paste&height=420&id=ubf748d56&originHeight=840&originWidth=1166&originalType=binary&ratio=1&rotation=0&showTitle=false&size=31767&status=error&style=none&taskId=u88049fc1-b9d3-473a-8f2e-b206408859f&title=&width=583)

讨论点：

- table 页面不满足flow页面的分叉逻辑。table展示分叉不直观。table页面是否保留。(不保留)
- 子流程在流程图上暂不实现
# 六、业务流程节点模型
## 1. 业务流程模型
```json
{
    "enabledFlag": 1,
    "tenantId": 41,
    "processCode": "CARRIER_ASSIGN_PROCESS",
    "processName": "配货单指定特殊承运商流程",
    "flowElements": [
        {
            "id": "9cecc8df-bec8-42c2-842a-3286dcb81ecd",
            "outgoing": [
                "node-1666341233049"
            ],
            "incoming": [
                "node-1666341184715"
            ],
            "type": "SEQUENCE_FLOW"
        },
        {
            "id": "ba4f6773-8e59-481a-b962-2a29912b47bb",
            "outgoing": [
                "node-1666750617267"
            ],
            "incoming": [
                "node-1666341233049"
            ],
            "type": "CONDITIONAL_FLOW",
            "ruleCode": "R20221026000002",
            "priority": 1
        },
        {
            "id": "48b53d3c-ef94-48d9-86bd-bd0268760804",
            "outgoing": [
                "node-1666750687119"
            ],
            "incoming": [
                "node-1666341233049"
            ],
            "type": "DEFAULT_FLOW"
        },
        {
            "id": "e0148645-6706-4f57-b3f8-4531c5fb2729",
            "outgoing": [
                "6180116a-3509-417e-a351-2afc3a9cdbda"
            ],
            "incoming": [
                "node-1666750687119"
            ],
            "type": "CONDITIONAL_FLOW",
            "ruleCode": "R20221026000003",
            "priority": 2
        },
        {
            "id": "e20f71d2-dc43-44b3-bb3c-e11050eab011",
            "outgoing": [
                "node-1666750740119"
            ],
            "incoming": [
                "node-1666750687119"
            ],
            "type": "DEFAULT_FLOW"
        },
        {
            "id": "19ef2a9b-0412-4540-9527-1426aeea8f75",
            "outgoing": [
                "5d51844f-fdfe-4a8e-9120-296f5b71d666"
            ],
            "incoming": [
                "node-1666750740119"
            ],
            "type": "CONDITIONAL_FLOW",
            "ruleCode": "R20221026000017",
            "priority": 3
        },
        {
            "id": "a299b0e1-e341-49fe-b962-aa598a2224a3",
            "outgoing": [
                "node-1666686019779"
            ],
            "incoming": [
                "node-1666750740119"
            ],
            "type": "DEFAULT_FLOW"
        },
        {
            "id": "3ad25d8b-a42e-4a3e-93ca-aaea8db4e30b",
            "outgoing": [
                "node-1666341254749"
            ],
            "incoming": [
                "node-1666750617267"
            ],
            "type": "SEQUENCE_FLOW"
        },
        {
            "id": "6176e02a-138e-463a-a5b4-ea22bd64cc8c",
            "outgoing": [
                "node-1666341254749"
            ],
            "incoming": [
                "6180116a-3509-417e-a351-2afc3a9cdbda"
            ],
            "type": "SEQUENCE_FLOW"
        },
        {
            "id": "9056956e-4fb0-446a-839b-2fcf462ee192",
            "outgoing": [
                "node-1666341254749"
            ],
            "incoming": [
                "5d51844f-fdfe-4a8e-9120-296f5b71d666"
            ],
            "type": "SEQUENCE_FLOW"
        },
        {
            "id": "0631aaae-be63-477b-86fc-b0574b80a12e",
            "outgoing": [
                "node-1666341254749"
            ],
            "incoming": [
                "node-1666686019779"
            ],
            "type": "SEQUENCE_FLOW"
        },
        {
            "id": "node-1666341233049",
            "outgoing": [
                "ba4f6773-8e59-481a-b962-2a29912b47bb",
                "48b53d3c-ef94-48d9-86bd-bd0268760804"
            ],
            "incoming": [
                "9cecc8df-bec8-42c2-842a-3286dcb81ecd"
            ],
            "type": "EXCLUSIVE_GATEWAY"
        },
        {
            "id": "node-1666341254749",
            "outgoing": [],
            "incoming": [
                "3ad25d8b-a42e-4a3e-93ca-aaea8db4e30b",
                "6176e02a-138e-463a-a5b4-ea22bd64cc8c",
                "9056956e-4fb0-446a-839b-2fcf462ee192",
                "0631aaae-be63-477b-86fc-b0574b80a12e"
            ],
            "type": "END_EVENT"
        },
        {
            "id": "5d51844f-fdfe-4a8e-9120-296f5b71d666",
            "outgoing": [
                "9056956e-4fb0-446a-839b-2fcf462ee192"
            ],
            "incoming": [
                "19ef2a9b-0412-4540-9527-1426aeea8f75"
            ],
            "type": "SERVICE_TASK",
            "beanId": "carrierAssignProcessNode",
            "enabledFlag": 1,
            "args": {
                "carrierCode": "EMS"
            }
        },
        {
            "id": "node-1666750740119",
            "outgoing": [
                "19ef2a9b-0412-4540-9527-1426aeea8f75",
                "a299b0e1-e341-49fe-b962-aa598a2224a3"
            ],
            "incoming": [
                "e20f71d2-dc43-44b3-bb3c-e11050eab011"
            ],
            "type": "EXCLUSIVE_GATEWAY"
        },
        {
            "id": "node-1666686019779",
            "outgoing": [
                "0631aaae-be63-477b-86fc-b0574b80a12e"
            ],
            "incoming": [
                "a299b0e1-e341-49fe-b962-aa598a2224a3"
            ],
            "type": "SERVICE_TASK",
            "beanId": "carrierAssignDefaultNode",
            "enabledFlag": 1,
            "args": {}
        },
        {
            "id": "node-1666341184715",
            "outgoing": [
                "9cecc8df-bec8-42c2-842a-3286dcb81ecd"
            ],
            "incoming": [],
            "type": "START_EVENT"
        },
        {
            "id": "6180116a-3509-417e-a351-2afc3a9cdbda",
            "outgoing": [
                "6176e02a-138e-463a-a5b4-ea22bd64cc8c"
            ],
            "incoming": [
                "e0148645-6706-4f57-b3f8-4531c5fb2729"
            ],
            "type": "SERVICE_TASK",
            "beanId": "carrierAssignProcessNode",
            "enabledFlag": 1,
            "args": {
                "carrierCode": "SF"
            }
        },
        {
            "id": "node-1666750687119",
            "outgoing": [
                "e0148645-6706-4f57-b3f8-4531c5fb2729",
                "e20f71d2-dc43-44b3-bb3c-e11050eab011"
            ],
            "incoming": [
                "48b53d3c-ef94-48d9-86bd-bd0268760804"
            ],
            "type": "EXCLUSIVE_GATEWAY"
        },
        {
            "id": "node-1666750617267",
            "outgoing": [
                "3ad25d8b-a42e-4a3e-93ca-aaea8db4e30b"
            ],
            "incoming": [
                "ba4f6773-8e59-481a-b962-2a29912b47bb"
            ],
            "type": "SERVICE_TASK",
            "beanId": "carrierAssignProcessNode",
            "enabledFlag": 1,
            "args": {
                "carrierCode": "JD"
            }
        }
    ]
}
```
## 2 业务节点详解

1. StartEvent 开始节点(一个业务模型只有一个开始节点)
```json
{
    "id": "node-1666341184715",   // 节点id, 一个流程内id唯一
    "outgoing": [
        "9cecc8df-bec8-42c2-842a-3286dcb81ecd"     // 目标 顺序线id
    ],
    "incoming": [],         // 来源线id
    "type": "START_EVENT"  // 元素类型：开始节点
}
```

2. serviceTask 服务任务(服务进行执行一段逻辑)
```json
{
    "id": "node-1666750617267",
    "outgoing": [
        "3ad25d8b-a42e-4a3e-93ca-aaea8db4e30b"
    ],
    "incoming": [
        "ba4f6773-8e59-481a-b962-2a29912b47bb"
    ],
    "type": "SERVICE_TASK",                     // 元素类型：服务任务节点
    "beanId": "carrierAssignProcessNode",       // 服务类的beanName
    "enabledFlag": 1,                          // 是否启用标志，1-启用，0-不启用 
    "args": {                      // 节点参数，参数Code前后端约定好
        "carrierCode": "JD"
    }
}
```

3. exclusiveGateway 排它型网关
```json
{
    "id": "node-1666750740119",
    "outgoing": [
        "19ef2a9b-0412-4540-9527-1426aeea8f75",     //目标线： 其中有且仅有一条默认线
        "a299b0e1-e341-49fe-b962-aa598a2224a3"      // 其他线为条件线 
    ],
    "incoming": [
        "e20f71d2-dc43-44b3-bb3c-e11050eab011"    // 来源线id，不限定类型
    ],
    "type": "EXCLUSIVE_GATEWAY"          // 元素类型：排它网关
}
```

4. sequenceFlow 顺序流 (来源不能是网关)
```json
{
    "id": "0631aaae-be63-477b-86fc-b0574b80a12e",
    "outgoing": [
        "node-1666341254749"             // 来源节点id
    ],
    "incoming": [
        "node-1666686019779"             // 目标节点id
    ],
    "type": "SEQUENCE_FLOW"              // 元素类型: 顺序线
}
```

5. conditionalFlow 条件流 (来源只能是网关)
```json
{
    "id": "e0148645-6706-4f57-b3f8-4531c5fb2729",
    "outgoing": [
        "6180116a-3509-417e-a351-2afc3a9cdbda"      // 目标节点id
    ],
    "incoming": [
        "node-1666750687119"                       // 来源节点id, 只能是网关类型节点
    ],
    "type": "CONDITIONAL_FLOW",                    // 元素类型：条件线
    "ruleCode": "R20221026000003",                 // 规则编码，通过编码调用规则引擎接口
    "priority": 2                                  // 条件线优先级，数字小的先执行
}
```

6. defaultFlow 缺省流/默认流 (来源只能是网关)
```json
{
    "id": "a299b0e1-e341-49fe-b962-aa598a2224a3",
    "outgoing": [
        "node-1666686019779"                       // 目标节点id
    ],
    "incoming": [
        "node-1666750740119"                      // 来源节点id
    ],
    "type": "DEFAULT_FLOW"                       //元素类型: 默认线
}
```

7. endEvent 结束节点(可以有多个)
```json
{
    "id": "node-1666341254749",
    "outgoing": [],
    "incoming": [
        "3ad25d8b-a42e-4a3e-93ca-aaea8db4e30b",
        "6176e02a-138e-463a-a5b4-ea22bd64cc8c",           // 来源线id
        "9056956e-4fb0-446a-839b-2fcf462ee192",
        "0631aaae-be63-477b-86fc-b0574b80a12e"
    ],
    "type": "END_EVENT"           //元素类型: 结束节点（一个业务流程中可以存在多个结束节点）
}
```
## 3. 前后端交互json
```json
{
    "cells": [
        {
            "position": {
                "x": 200,
                "y": 180
            },
            "size": {
                "width": 210,
                "height": 64
            },
            "view": "react-shape-view",
            "shape": "start-node",
            "ports": {
                "groups": {
                    "top": {
                        "position": "top",
                        "attrs": {
                            "circle": {
                                "r": 4,
                                "magnet": true,
                                "stroke": "#1f74ff",
                                "strokeWidth": 1,
                                "fill": "#fff",
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    "right": {
                        "position": "right",
                        "attrs": {
                            "circle": {
                                "r": 4,
                                "magnet": true,
                                "stroke": "#1f74ff",
                                "strokeWidth": 1,
                                "fill": "#fff",
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    "bottom": {
                        "position": "bottom",
                        "attrs": {
                            "circle": {
                                "r": 4,
                                "magnet": true,
                                "stroke": "#1f74ff",
                                "strokeWidth": 1,
                                "fill": "#fff",
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    "left": {
                        "position": "left",
                        "attrs": {
                            "circle": {
                                "r": 4,
                                "magnet": true,
                                "stroke": "#1f74ff",
                                "strokeWidth": 1,
                                "fill": "#fff",
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    }
                },
                "items": [
                    {
                        "group": "top",
                        "id": "ee53fa04-1273-48fc-9baf-212a0d33d0c7",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    {
                        "group": "top",
                        "id": "ece5b9bf-ab64-461b-a7fe-1e8e3c098dff",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    {
                        "group": "right",
                        "id": "6a272205-37ab-4b90-bd8d-aa105d99236c",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    {
                        "group": "bottom",
                        "id": "6741742e-366e-4ab8-bcf9-9ee663f75225",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    {
                        "group": "bottom",
                        "id": "5c67d513-d618-4154-8351-55fd54066c7a",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    {
                        "group": "left",
                        "id": "5053c48c-af11-4726-a6b5-68475359629d",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    }
                ]
            },
            "id": "node-1666150094860",
            "data": {
                "beanId": null,
                "nodeName": null,
                "showNodeName": null,
                "enabledFlag": 0,
                "argsInfo": {},
                "args": {},
                "isDragGenerate": false
            },
            "zIndex": 1
        },
        {
            "position": {
                "x": 200,
                "y": 610.5
            },
            "size": {
                "width": 210,
                "height": 64
            },
            "view": "react-shape-view",
            "shape": "flow-node",
            "ports": {
                "groups": {
                    "top": {
                        "position": "top",
                        "attrs": {
                            "circle": {
                                "r": 4,
                                "magnet": true,
                                "stroke": "#1f74ff",
                                "strokeWidth": 1,
                                "fill": "#fff",
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    "right": {
                        "position": "right",
                        "attrs": {
                            "circle": {
                                "r": 4,
                                "magnet": true,
                                "stroke": "#1f74ff",
                                "strokeWidth": 1,
                                "fill": "#fff",
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    "bottom": {
                        "position": "bottom",
                        "attrs": {
                            "circle": {
                                "r": 4,
                                "magnet": true,
                                "stroke": "#1f74ff",
                                "strokeWidth": 1,
                                "fill": "#fff",
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    "left": {
                        "position": "left",
                        "attrs": {
                            "circle": {
                                "r": 4,
                                "magnet": true,
                                "stroke": "#1f74ff",
                                "strokeWidth": 1,
                                "fill": "#fff",
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    }
                },
                "items": [
                    {
                        "group": "top",
                        "id": "93652672-5508-401c-ae14-a83817ffdd32",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    {
                        "group": "top",
                        "id": "80b6739c-8fd8-4544-ae2a-e14870059f49",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    {
                        "group": "right",
                        "id": "aa864b92-131e-4746-a3e2-157fdc8b69f5",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    {
                        "group": "bottom",
                        "id": "2287ba44-d50c-45fe-a950-aced61f79b68",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    {
                        "group": "bottom",
                        "id": "65341b2e-63bf-4d5a-a2fa-c127e51686e0",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    {
                        "group": "left",
                        "id": "ebab1970-c934-4983-985a-6d315742bcee",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    }
                ]
            },
            "id": "node-1666664535592",
            "data": {
                "isDragGenerate": false,
                "beanId": "carrierAssignDefaultNode",
                "nodeName": "触发下一流程",
                "enabledFlag": 1,
                "args": {},
                "argsInfo": null,
                "description": "触发承运商解析流程",
                "showNodeName": "触发下一流程"
            },
            "zIndex": 26
        },
        {
            "position": {
                "x": 200,
                "y": 337.66668701171875
            },
            "size": {
                "width": 210,
                "height": 64
            },
            "view": "react-shape-view",
            "shape": "branch-node",
            "ports": {
                "groups": {
                    "top": {
                        "position": "top",
                        "attrs": {
                            "circle": {
                                "r": 4,
                                "magnet": true,
                                "stroke": "#1f74ff",
                                "strokeWidth": 1,
                                "fill": "#fff",
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    "right": {
                        "position": "right",
                        "attrs": {
                            "circle": {
                                "r": 4,
                                "magnet": true,
                                "stroke": "#1f74ff",
                                "strokeWidth": 1,
                                "fill": "#fff",
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    "bottom": {
                        "position": "bottom",
                        "attrs": {
                            "circle": {
                                "r": 4,
                                "magnet": true,
                                "stroke": "#1f74ff",
                                "strokeWidth": 1,
                                "fill": "#fff",
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    "left": {
                        "position": "left",
                        "attrs": {
                            "circle": {
                                "r": 4,
                                "magnet": true,
                                "stroke": "#1f74ff",
                                "strokeWidth": 1,
                                "fill": "#fff",
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    }
                },
                "items": [
                    {
                        "group": "top",
                        "id": "6496f688-d4b9-4ff1-96b0-12acba09a7c4",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    {
                        "group": "top",
                        "id": "42ff2048-b6a2-4551-a936-ec1faf067f29",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    {
                        "group": "right",
                        "id": "0e57e3c3-2d18-4f6b-a89f-86db9b9bf07b",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    {
                        "group": "bottom",
                        "id": "efcf6c91-f9f8-4f3f-a405-9dcca9fe68a1",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    {
                        "group": "bottom",
                        "id": "b6dfd358-da64-4c27-9118-e74fbeca9716",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    {
                        "group": "left",
                        "id": "7f84ce31-dfbe-45f4-a09e-41cf28c4596d",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    }
                ]
            },
            "id": "node-1667195849574",
            "data": {
                "beanId": null,
                "nodeName": null,
                "showNodeName": null,
                "enabledFlag": 0,
                "argsInfo": {},
                "args": {},
                "isDragGenerate": false
            },
            "zIndex": 27
        },
        {
            "position": {
                "x": 618.888916015625,
                "y": 317.66668701171875
            },
            "size": {
                "width": 210,
                "height": 64
            },
            "view": "react-shape-view",
            "shape": "flow-node",
            "ports": {
                "groups": {
                    "top": {
                        "position": "top",
                        "attrs": {
                            "circle": {
                                "r": 4,
                                "magnet": true,
                                "stroke": "#1f74ff",
                                "strokeWidth": 1,
                                "fill": "#fff",
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    "right": {
                        "position": "right",
                        "attrs": {
                            "circle": {
                                "r": 4,
                                "magnet": true,
                                "stroke": "#1f74ff",
                                "strokeWidth": 1,
                                "fill": "#fff",
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    "bottom": {
                        "position": "bottom",
                        "attrs": {
                            "circle": {
                                "r": 4,
                                "magnet": true,
                                "stroke": "#1f74ff",
                                "strokeWidth": 1,
                                "fill": "#fff",
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    "left": {
                        "position": "left",
                        "attrs": {
                            "circle": {
                                "r": 4,
                                "magnet": true,
                                "stroke": "#1f74ff",
                                "strokeWidth": 1,
                                "fill": "#fff",
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    }
                },
                "items": [
                    {
                        "group": "top",
                        "id": "be32ad1a-f1c9-4250-a12e-fb849a641500",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    {
                        "group": "top",
                        "id": "5ee25c12-bf62-48bc-8a43-a8227e8e9225",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    {
                        "group": "right",
                        "id": "a3a21e6d-fa95-4f08-a6d4-ab36d847cf3e",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    {
                        "group": "bottom",
                        "id": "f1d0f87f-37d2-47ca-add0-5142ed301043",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    {
                        "group": "bottom",
                        "id": "a1f9cd15-7d12-4341-85f8-c1edb55d117e",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    {
                        "group": "left",
                        "id": "7e5fc841-b7b2-4c75-83f1-42047fcd05b2",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    }
                ]
            },
            "id": "node-1667195855110",
            "data": {
                "isDragGenerate": false,
                "beanId": "carrierAssignProcessNode",
                "nodeName": "指定特殊承运商",
                "enabledFlag": 1,
                "args": {
                    "carrierCode": "JD"
                },
                "argsInfo": [
                    {
                        "creationDate": "2022-10-19 11:27:26",
                        "createdBy": 582832,
                        "lastUpdateDate": "2022-10-24 16:52:52",
                        "lastUpdatedBy": 46,
                        "objectVersionNumber": 4,
                        "_token": "DifUN97HbEUSvq3i5TYdHKk82uegL1pDtIcjaRk1B3PGSInDRHYym2Z7WYdFML/7Si02gSZSS5KOb0p3aV4XPb8X5ZRjhj6mL5b2+dS2e5rm3tEyKaIKxk1iDUlcQBwAA/wPi5VhO7HjEnfPk6nXIQ==",
                        "bizNodeParameterId": 63,
                        "paramCode": "carrierCode",
                        "paramName": "承运商编码",
                        "beanId": "carrierAssignProcessNode",
                        "paramFormatCode": "TEXT",
                        "paramEditTypeCode": "INPUT",
                        "notnullFlag": 1,
                        "businessModel": null,
                        "valueFiledFrom": null,
                        "valueFiledTo": null,
                        "showFlag": 1,
                        "enabledFlag": 1,
                        "defaultValue": null,
                        "defaultMeaning": null,
                        "parentField": null,
                        "defaultValueType": null,
                        "tenantId": 2,
                        "paramFormatMeaning": "文本",
                        "paramEditTypeMeaning": "文本"
                    }
                ],
                "description": "指定特殊承运商(触发承运商解析流程)",
                "showNodeName": "指定发货承运商为京东"
            },
            "zIndex": 28
        },
        {
            "shape": "flow-edge",
            "id": "7ed1a8f7-f2b5-40c5-945c-79d87ead35d1",
            "zIndex": 29,
            "data": {
                "edgeType": "rule",
                "priority": 1,
                "ruleCode": "R20221031002",
                "ruleName": "当网店为京东时"
            },
            "labels": [
                "当网店为京东时"
            ],
            "source": {
                "cell": "node-1667195849574",
                "port": "0e57e3c3-2d18-4f6b-a89f-86db9b9bf07b"
            },
            "target": {
                "cell": "node-1667195855110",
                "port": "7e5fc841-b7b2-4c75-83f1-42047fcd05b2"
            }
        },
        {
            "position": {
                "x": 200,
                "y": 455.77777099609375
            },
            "size": {
                "width": 210,
                "height": 64
            },
            "view": "react-shape-view",
            "shape": "branch-node",
            "ports": {
                "groups": {
                    "top": {
                        "position": "top",
                        "attrs": {
                            "circle": {
                                "r": 4,
                                "magnet": true,
                                "stroke": "#1f74ff",
                                "strokeWidth": 1,
                                "fill": "#fff",
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    "right": {
                        "position": "right",
                        "attrs": {
                            "circle": {
                                "r": 4,
                                "magnet": true,
                                "stroke": "#1f74ff",
                                "strokeWidth": 1,
                                "fill": "#fff",
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    "bottom": {
                        "position": "bottom",
                        "attrs": {
                            "circle": {
                                "r": 4,
                                "magnet": true,
                                "stroke": "#1f74ff",
                                "strokeWidth": 1,
                                "fill": "#fff",
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    "left": {
                        "position": "left",
                        "attrs": {
                            "circle": {
                                "r": 4,
                                "magnet": true,
                                "stroke": "#1f74ff",
                                "strokeWidth": 1,
                                "fill": "#fff",
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    }
                },
                "items": [
                    {
                        "group": "top",
                        "id": "483ac986-7a04-498e-acc7-b38de84e7b24",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    {
                        "group": "top",
                        "id": "5c8b9da4-eb65-4b2d-9520-1b2be291829a",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    {
                        "group": "right",
                        "id": "5c35329d-c396-4be8-af11-02770f063d4d",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    {
                        "group": "bottom",
                        "id": "a3edddfa-ab43-4447-97b8-1f5deb15669b",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    {
                        "group": "bottom",
                        "id": "62cfea6e-d820-47ea-8787-9b52bbbe8e49",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    {
                        "group": "left",
                        "id": "9e41f1ed-bcee-4495-b413-a16f8774b92d",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    }
                ]
            },
            "id": "node-1667195945677",
            "data": {
                "beanId": null,
                "nodeName": null,
                "showNodeName": null,
                "enabledFlag": 0,
                "argsInfo": {},
                "args": {},
                "isDragGenerate": false
            },
            "zIndex": 30
        },
        {
            "position": {
                "x": 618.888916015625,
                "y": 435.77777099609375
            },
            "size": {
                "width": 210,
                "height": 64
            },
            "view": "react-shape-view",
            "shape": "flow-node",
            "ports": {
                "groups": {
                    "top": {
                        "position": "top",
                        "attrs": {
                            "circle": {
                                "r": 4,
                                "magnet": true,
                                "stroke": "#1f74ff",
                                "strokeWidth": 1,
                                "fill": "#fff",
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    "right": {
                        "position": "right",
                        "attrs": {
                            "circle": {
                                "r": 4,
                                "magnet": true,
                                "stroke": "#1f74ff",
                                "strokeWidth": 1,
                                "fill": "#fff",
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    "bottom": {
                        "position": "bottom",
                        "attrs": {
                            "circle": {
                                "r": 4,
                                "magnet": true,
                                "stroke": "#1f74ff",
                                "strokeWidth": 1,
                                "fill": "#fff",
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    "left": {
                        "position": "left",
                        "attrs": {
                            "circle": {
                                "r": 4,
                                "magnet": true,
                                "stroke": "#1f74ff",
                                "strokeWidth": 1,
                                "fill": "#fff",
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    }
                },
                "items": [
                    {
                        "group": "top",
                        "id": "332b8cc3-ea32-4771-b79a-f5f1a5ea2e92",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    {
                        "group": "top",
                        "id": "d5c5c34a-51e1-4d02-9286-44e2d4c1c159",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    {
                        "group": "right",
                        "id": "0209e1b3-3fec-47d2-9f33-a013346c870c",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    {
                        "group": "bottom",
                        "id": "351b6b8a-f5f7-4562-80ba-942a02d5e5e3",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    {
                        "group": "bottom",
                        "id": "619b85b6-4e67-4f1e-a0f8-3ce8464472ef",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    {
                        "group": "left",
                        "id": "61ed0114-ead9-4556-8d86-9dc29c8d7344",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    }
                ]
            },
            "id": "node-1667196047600",
            "data": {
                "isDragGenerate": false,
                "beanId": "carrierAssignProcessNode",
                "nodeName": "指定特殊承运商",
                "enabledFlag": 1,
                "args": {
                    "carrierCode": "EMS"
                },
                "argsInfo": [
                    {
                        "creationDate": "2022-10-19 11:27:26",
                        "createdBy": 582832,
                        "lastUpdateDate": "2022-10-24 16:52:52",
                        "lastUpdatedBy": 46,
                        "objectVersionNumber": 4,
                        "_token": "DifUN97HbEUSvq3i5TYdHKk82uegL1pDtIcjaRk1B3PGSInDRHYym2Z7WYdFML/7Si02gSZSS5KOb0p3aV4XPb8X5ZRjhj6mL5b2+dS2e5rm3tEyKaIKxk1iDUlcQBwAA/wPi5VhO7HjEnfPk6nXIQ==",
                        "bizNodeParameterId": 63,
                        "paramCode": "carrierCode",
                        "paramName": "承运商编码",
                        "beanId": "carrierAssignProcessNode",
                        "paramFormatCode": "TEXT",
                        "paramEditTypeCode": "INPUT",
                        "notnullFlag": 1,
                        "businessModel": null,
                        "valueFiledFrom": null,
                        "valueFiledTo": null,
                        "showFlag": 1,
                        "enabledFlag": 1,
                        "defaultValue": null,
                        "defaultMeaning": null,
                        "parentField": null,
                        "defaultValueType": null,
                        "tenantId": 2,
                        "paramFormatMeaning": "文本",
                        "paramEditTypeMeaning": "文本"
                    }
                ],
                "description": "指定特殊承运商(触发承运商解析流程)",
                "showNodeName": "指定发货承运商为EMS"
            },
            "zIndex": 32
        },
        {
            "shape": "flow-edge",
            "id": "7c2a1722-c81e-420d-ae49-35f8c344cde6",
            "zIndex": 33,
            "data": {
                "edgeType": "rule",
                "ruleCode": "R20221031003",
                "ruleName": "当收货地址为偏远地区时",
                "priority": 2
            },
            "labels": [
                "当收货地址为偏远地区时"
            ],
            "source": {
                "cell": "node-1667195945677",
                "port": "5c35329d-c396-4be8-af11-02770f063d4d"
            },
            "target": {
                "cell": "node-1667196047600",
                "port": "61ed0114-ead9-4556-8d86-9dc29c8d7344"
            }
        },
        {
            "shape": "flow-edge",
            "id": "5623c7ea-1e7d-4fc4-b0f1-7cbd3d83d82f",
            "zIndex": 34,
            "data": {
                "edgeType": "common"
            },
            "source": {
                "cell": "node-1667195945677",
                "port": "a3edddfa-ab43-4447-97b8-1f5deb15669b"
            },
            "target": {
                "cell": "node-1666664535592",
                "port": "93652672-5508-401c-ae14-a83817ffdd32"
            }
        },
        {
            "position": {
                "x": 200,
                "y": 755.006591796875
            },
            "size": {
                "width": 210,
                "height": 64
            },
            "view": "react-shape-view",
            "shape": "end-node",
            "ports": {
                "groups": {
                    "top": {
                        "position": "top",
                        "attrs": {
                            "circle": {
                                "r": 4,
                                "magnet": true,
                                "stroke": "#1f74ff",
                                "strokeWidth": 1,
                                "fill": "#fff",
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    "right": {
                        "position": "right",
                        "attrs": {
                            "circle": {
                                "r": 4,
                                "magnet": true,
                                "stroke": "#1f74ff",
                                "strokeWidth": 1,
                                "fill": "#fff",
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    "bottom": {
                        "position": "bottom",
                        "attrs": {
                            "circle": {
                                "r": 4,
                                "magnet": true,
                                "stroke": "#1f74ff",
                                "strokeWidth": 1,
                                "fill": "#fff",
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    "left": {
                        "position": "left",
                        "attrs": {
                            "circle": {
                                "r": 4,
                                "magnet": true,
                                "stroke": "#1f74ff",
                                "strokeWidth": 1,
                                "fill": "#fff",
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    }
                },
                "items": [
                    {
                        "group": "top",
                        "id": "faf3b1ad-ec45-46d4-a80c-0021014c7894",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    {
                        "group": "top",
                        "id": "5ffb1e15-d65d-413f-8b75-d8ed2b9247db",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    {
                        "group": "right",
                        "id": "3f9a44da-11a4-42f2-9083-4b16805e31ed",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    {
                        "group": "bottom",
                        "id": "1f25d749-8253-48de-b776-cc902ee3900e",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    {
                        "group": "bottom",
                        "id": "532845c1-e66f-448d-b01a-11a06ddb7232",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    },
                    {
                        "group": "left",
                        "id": "39f9cec1-e793-46b0-8ed2-b0ba95492841",
                        "attrs": {
                            "circle": {
                                "style": {
                                    "visibility": "hidden"
                                }
                            }
                        }
                    }
                ]
            },
            "id": "node-1667196135753",
            "data": {
                "beanId": null,
                "nodeName": null,
                "showNodeName": null,
                "enabledFlag": 0,
                "argsInfo": {},
                "args": {},
                "isDragGenerate": false
            },
            "zIndex": 35
        },
        {
            "shape": "flow-edge",
            "id": "61d0637d-3cd7-40d6-b652-cd9be5398b08",
            "zIndex": 36,
            "source": {
                "cell": "node-1666664535592",
                "port": "2287ba44-d50c-45fe-a950-aced61f79b68"
            },
            "target": {
                "cell": "node-1667196135753",
                "port": "3f9a44da-11a4-42f2-9083-4b16805e31ed"
            }
        },
        {
            "shape": "flow-edge",
            "id": "c2459300-d32c-41b6-94ec-eb6059d9e142",
            "zIndex": 37,
            "source": {
                "cell": "node-1667196047600",
                "port": "0209e1b3-3fec-47d2-9f33-a013346c870c"
            },
            "target": {
                "cell": "node-1667196135753",
                "port": "3f9a44da-11a4-42f2-9083-4b16805e31ed"
            }
        },
        {
            "shape": "flow-edge",
            "id": "67884508-6885-4cd4-93f5-886f43ea0ffa",
            "zIndex": 38,
            "source": {
                "cell": "node-1667195855110",
                "port": "a3a21e6d-fa95-4f08-a6d4-ab36d847cf3e"
            },
            "target": {
                "cell": "node-1667196135753",
                "port": "3f9a44da-11a4-42f2-9083-4b16805e31ed"
            }
        },
        {
            "shape": "flow-edge",
            "id": "4627dc09-20d7-448b-b527-57bdd518e153",
            "zIndex": 39,
            "source": {
                "cell": "node-1666150094860",
                "port": "6741742e-366e-4ab8-bcf9-9ee663f75225"
            },
            "target": {
                "cell": "node-1667195849574",
                "port": "6496f688-d4b9-4ff1-96b0-12acba09a7c4"
            }
        },
        {
            "shape": "flow-edge",
            "attrs": {},
            "id": "883da5a0-d883-438c-9955-ef72e977b936",
            "source": {
                "cell": "node-1667195849574",
                "port": "efcf6c91-f9f8-4f3f-a405-9dcca9fe68a1"
            },
            "target": {
                "cell": "node-1667195945677",
                "port": "483ac986-7a04-498e-acc7-b38de84e7b24"
            },
            "zIndex": 40,
            "data": {
                "edgeType": "common"
            }
        }
    ]
}
```
# 七、代码设计

1. 设计流程元素类FlowElement，设计开始事件 startEvent，Task，Flow， gateway，endEvent五个子类

![](https://cdn.nlark.com/yuque/0/2022/jpeg/29446496/1667530914398-8578bbef-8cf4-46e3-b2ee-61c268340d9a.jpeg)

2. 业务流程模型合理性校验
-  模型合法性校验入口：BpmnModelValidator.validate
-  checkElement：对流程内所有元素进行校验校验startEvent节点有且只有一个，EndEvent节点存在。然后 循环校验流程元素，每个元素有自己的校验逻辑，详情请看下图
-  checkCycle：是否存在死循环

![](https://cdn.nlark.com/yuque/0/2022/jpeg/29446496/1667533388389-641b8a67-dfca-45c8-852a-6f00d6784664.jpeg)

3. 业务模型的流转实现

![](https://cdn.nlark.com/yuque/0/2022/jpeg/29446496/1664332884353-bceb8ee1-a31c-4a9b-9a9c-5b94d251f25c.jpeg)

4. 网关和conditionflow 逻辑实现：

网关通过寻找自己下面所有的条件线，然后对条件线进行排序，然后更新序号小的优先执行，调用规则中心的接口，直到拿到结果为true则中断并找到这条结果线的目标节点，如果所有条件线的结果为false, 则直接寻找默认线的目标节点。

5. 线的实现

       线没有实现，只为来源节点提供目标节点的信息

6. 流程模型解析的实现 :

阿里和滴滴都是通过xml解析的方式来组装具体的流程模型对象，O2BPM通过解析前端传来的前端流程图json来得到后端自己的业务模型。具体解析过程请看org.o2.business.process.management.infra.convert.ViewJsonConvert#viewJsonConvert

7. 流程顺序的实现(通过顺序流的来源节点和目标节点进行整个流程的执行，初始节点为  StartEvent 开始节点， 有且只有一个)

# 参考文档
[osworkflow、jbpm、activiti、flowable、camunda开源流程引擎哪个好?_大龄码农有梦想的博客-CSDN博客_activiti和camunda](https://blog.csdn.net/wxz258/article/details/116405594)
[阿里开源流程引擎，轻松干掉 activity 、flowable_码农小胖哥的博客-CSDN博客](https://felord.blog.csdn.net/article/details/118837330?spm=1001.2101.3001.6661.1&utm_medium=distribute.pc_relevant_t0.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-1-118837330-blog-116405594.pc_relevant_multi_platform_whitelistv3&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-1-118837330-blog-116405594.pc_relevant_multi_platform_whitelistv3&utm_relevant_index=1)
[Camunda/Flowable/Activiti技术发展史_分享牛的博客-CSDN博客_activiti camunda](https://blog.csdn.net/qq_30739519/article/details/86583765?spm=1001.2101.3001.6650.5&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-5-86583765-blog-116405594.pc_relevant_multi_platform_whitelistv3&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-5-86583765-blog-116405594.pc_relevant_multi_platform_whitelistv3&utm_relevant_index=9)