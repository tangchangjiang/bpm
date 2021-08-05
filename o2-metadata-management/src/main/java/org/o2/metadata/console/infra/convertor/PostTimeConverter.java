package org.o2.metadata.console.infra.convertor;

import org.o2.metadata.console.api.vo.PostTimeVO;
import org.o2.metadata.console.infra.entity.PostTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 *
 * 服务点时间
 *
 * @author yipeng.zhu@hand-china.com 2021-08-04
 **/
public class PostTimeConverter{
    private PostTimeConverter() {
        // 无需实现
    }

    /**
     *  po 转 vo
     * @param postTimeList 服务点时间集合
     * @return  list
     */
    public static List<PostTimeVO> toPostTimeVOList(List<PostTime> postTimeList) {
        if (postTimeList == null) {
            return Collections.emptyList();
        }
        List<PostTimeVO> postTimeVOList = new ArrayList<>();
        for (PostTime postTime : postTimeList) {
            postTimeVOList.add(toPostTimeVO(postTime));
        }
        return postTimeVOList;
    }
   /**
    * po 转 vo
    * @param postTime 服务点时间
    * @return  vo
    */
    private static PostTimeVO toPostTimeVO(PostTime postTime) {
        if (postTime == null) {
            return null;
        }
        PostTimeVO postTimeVO = new PostTimeVO();
        postTimeVO.setPostTimeId(postTime.getPostTimeId());
        postTimeVO.setPosId(postTime.getPosId());
        postTimeVO.setWeek(postTime.getWeek());
        postTimeVO.setReceiveStartTime(postTime.getReceiveStartTime());
        postTimeVO.setReceiveEndTime(postTime.getReceiveEndTime());
        postTimeVO.setDistributeStartTime(postTime.getDistributeStartTime());
        postTimeVO.setDistributeEndTime(postTime.getDistributeEndTime());
        postTimeVO.setTenantId(postTime.getTenantId());
        postTimeVO.setCreationDate(postTime.getCreationDate());
        postTimeVO.setCreatedBy(postTime.getCreatedBy());
        postTimeVO.setLastUpdateDate(postTime.getLastUpdateDate());
        postTimeVO.setLastUpdatedBy(postTime.getLastUpdatedBy());
        postTimeVO.setObjectVersionNumber(postTime.getObjectVersionNumber());
        postTimeVO.set_token(postTime.get_token());
        return postTimeVO;
    }
}
