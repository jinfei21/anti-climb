package com.yjfei.antibot.service;


import com.yjfei.antibot.bean.ModelBean;
import com.yjfei.antibot.bean.ModelFieldBean;
import com.yjfei.antibot.common.RiskException;
import com.yjfei.antibot.dao.ModelFieldMapper;
import com.yjfei.antibot.dao.ModelMapper;
import com.yjfei.antibot.data.DataModel;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DataModelService {


    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ModelFieldMapper modelFieldMapper;


    public DataModel getDataModelById(long modelId) {

        ModelBean modelBean = modelMapper.selectById(modelId);

        List<ModelFieldBean> modelFieldBeanList = modelFieldMapper.selectByModelId(modelBean.getId());

        if (CollectionUtils.isEmpty(modelFieldBeanList)) {
            throw new RiskException("数据模型ID（" + modelBean.getId() + "）没有字段信息");
        }

        return new DataModel(this, modelBean, modelFieldBeanList);

    }

    @Transactional
    public void addModel(ModelBean modelBean, List<ModelFieldBean> modelFieldBeanList) {
        if (modelBean.getId() == null) {
            modelBean.setCreateBy("admin");
            modelBean.setUpdateBy(modelBean.getCreateBy());
            modelMapper.insert(modelBean);

            for (ModelFieldBean modelFieldBean : modelFieldBeanList) {
                modelFieldBean.setModelId(modelBean.getId());
                modelFieldBean.setCreateBy("admin");
                modelFieldBean.setUpdateBy(modelFieldBean.getCreateBy());
                modelFieldMapper.insert(modelFieldBean);
            }

        } else {
            modelBean.setUpdateBy("admin");
            modelMapper.updateByPrimaryKeySelective(modelBean);

            List<ModelFieldBean> oldModelFieldList = modelFieldMapper.selectByModelId(modelBean.getId());

            Map<Long, ModelFieldBean> needDeleteModelFieldMap = oldModelFieldList.stream().collect(Collectors.toMap(ModelFieldBean::getId, modelFieldBean -> modelFieldBean));

            modelFieldBeanList.forEach((modelFieldBean -> {
                modelFieldBean.setModelId(modelBean.getId());

                if (modelFieldBean.getId() == null){
                    modelFieldBean.setCreateBy("admin");
                    modelFieldBean.setUpdateBy(modelFieldBean.getCreateBy());
                    modelFieldMapper.insert(modelFieldBean);
                }else {
                    needDeleteModelFieldMap.remove(modelFieldBean.getId());
                    modelFieldBean.setUpdateBy("admin");
                    modelFieldMapper.updateByPrimaryKeySelective(modelFieldBean);
                }
            }));

            if (!CollectionUtils.isEmpty(needDeleteModelFieldMap)){
                needDeleteModelFieldMap.forEach((id,modelFieldBean)->{
                    modelFieldMapper.deleteByPrimaryKey(id);
                });
            }
        }
    }

    @Transactional
    public void deleteDataModel(Long id){
        modelMapper.deleteByPrimaryKey(id);
        modelFieldMapper.deleteByModelId(id);

    }

    public ModelBean findModelById(Long modelId){
        return modelMapper.selectByPrimaryKey(modelId);
    }

    public List<ModelFieldBean> findModelFieldByModelId(Long modelId){
        return modelFieldMapper.selectByModelId(modelId);
    }

    public Page<ModelBean> list(Integer page, Integer pageSize) {
        Page<ModelBean> pageResult = PageHelper.startPage(page, pageSize)
                .doSelectPage(() -> modelMapper.selectAll());

        return pageResult;
    }


}
