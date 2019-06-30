import { User } from './user';
import { TaskDetails } from './task-details';

export class ProjectDetails {
    projectId: number = 0;
    project: string;
    startDate: string;
    endDate: string;
    priority: number;
    manager: User;
    tasks: Array<TaskDetails>;
    status: string;
}
